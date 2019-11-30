package uk.me.jasonmarston.mvc.controller;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import uk.me.jasonmarston.domain.aggregate.impl.ResetToken;
import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.service.ResetTokenService;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;
import uk.me.jasonmarston.mvc.event.OnForgottenPasswordEvent;
import uk.me.jasonmarston.mvc.event.OnRegistrationCompleteEvent;

@Controller
public class AuthenticationController {
	@Autowired
	private VerificationTokenService verificationTokenService;
	
	@Autowired
	private ResetTokenService resetTokenService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/forgotten")
	public String forgotten() {
		return "authentication/forgotten";
	}
	
	@PostMapping("/forgotten")
	public String forgottenAction(
			@ModelAttribute("username") final String email,
			final WebRequest request) {
		applicationEventPublisher
				.publishEvent(new OnForgottenPasswordEvent(
						email, 
						request.getContextPath()));

		return "authentication/forgottenConfirmation";
	}

	@GetMapping("/forgotten/confirmation")
	public String forgottenConfirmation(
			final WebRequest request, 
			@RequestParam(value = "token", required = false) final String token) {
		final ResetToken resetToken = 
				resetTokenService.findByToken(token);

		if(resetToken == null) {
			return "redirect:/forgotten?expired";
		}

		final User user = userService.getUser(resetToken.getUserId());
		if(user == null) {
			return "redirect:/forgotten?expired";
		}

		if(isExpired(resetToken.getExpiryDate())) {
			resetTokenService.delete(resetToken);
			return "redirect:/forgotten?expired";
		}
		
		updateUser(user);
		resetTokenService.delete(resetToken);

	    return "authentication/forgottenReset";
	}

	@PostMapping("/forgotten/reset")
	public String forgottenReset(
			@ModelAttribute("password") final String password,
			@ModelAttribute("passwordConfirmation")
					final String passwordConfirmation) {
		final User user = userService.getUser(getUserId());
		user.setPassword(passwordEncoder.encode(password));
		userService.update(user);
		updateUser(user);

	    return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "authentication/login";
	}

	@GetMapping("/registration")
	public String registration() {
		return "authentication/registration";
	}
	
	@PostMapping("/registration")
	public String registrationAction(
			@ModelAttribute("username") final String email,
			@ModelAttribute("password") final String password,
			@ModelAttribute("passwordConfirmation") 
					final String passwordConfirmation,
			final WebRequest request) {
		try {
			final User user = userService.signUp(
					email,
					password,
					passwordConfirmation);
			applicationEventPublisher
					.publishEvent(new OnRegistrationCompleteEvent(
							user, 
							request.getContextPath()));
		}
		catch(InvalidParameterException e) {
			return "redirect:/registration?registration";
		}
		return "authentication/registrationConfirmation";
	}
	
	@GetMapping("/registration/confirmation")
	public String registrationConfirmation(
			final WebRequest request, 
			@RequestParam(value = "token", required = false) final String token) {
		final VerificationToken verificationToken = 
				verificationTokenService.findByToken(token);

		if(verificationToken == null) {
			return "redirect:/registration?expired";
		}

		final User user = userService.getUser(verificationToken.getUserId());
		if(user == null) {
			return "redirect:/registration?expired";
		}

		if(isExpired(verificationToken.getExpiryDate())) {
			verificationTokenService.delete(verificationToken);
			return "redirect:/registration?expired";
		}

		user.setEnabled(true);
		userService.update(user);
		updateUser(user);
		verificationTokenService.delete(verificationToken);
	    
		return "redirect:/";
	}

	private User getUser() {
		return (User)SecurityContextHolder
				.getContext()
				.getAuthentication()
				.getPrincipal();
	}

	private EntityId getUserId() {
		return getUser().getId();
	}

	private boolean isExpired(final Date exipry) {
		final Calendar cal = Calendar.getInstance();
		final long now = cal.getTime().getTime();
		final long timeRemaining = exipry.getTime() - now;
		return timeRemaining <= 0;
	}
	
	private void updateUser(final User user) {
		final Authentication authentication = 
				new UsernamePasswordAuthenticationToken(
						user,
						user.getPassword(),
						user.getAuthorities());
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}