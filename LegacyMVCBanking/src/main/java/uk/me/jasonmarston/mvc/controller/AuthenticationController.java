package uk.me.jasonmarston.mvc.controller;

import java.security.InvalidParameterException;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.mvc.event.OnRegistrationCompleteEvent;

@Controller
public class AuthenticationController {
	@Autowired
	private VerificationTokenService verificationTokenService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@GetMapping("/login")
	public String login() {
		return "authentication/login";
	}
	
	@GetMapping("/signup")
	public String signup() {
		return "authentication/signup";
	}
	
	@GetMapping("/confirm")
	public String confirm(
			final WebRequest request, 
			@RequestParam(value = "token", required = false) final String token) {
		final VerificationToken verificationToken = 
				verificationTokenService.findByToken(token);
		if(verificationToken == null) {
			return "redirect:/signup?expired";
		}

		final User user = userService.getUser(verificationToken.getUserId());

		final long expiry = verificationToken.getExpiryDate().getTime();
		final Calendar cal = Calendar.getInstance();
		final long now = cal.getTime().getTime();
		final long timeRemaining = expiry - now;
		if(timeRemaining <= 0) {
			verificationTokenService.delete(verificationToken);
			return "redirect:/signup?expired";
		}

		user.setEnabled(true);
		userService.update(user);
		
		verificationTokenService.delete(verificationToken);
		
		final Authentication authentication = 
				new UsernamePasswordAuthenticationToken(
						user,
						user.getPassword(),
						user.getAuthorities());
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    
		return "redirect:/";
	}
	
	@GetMapping("/reset")
	public String resetPassword() {
		return "reset";
	}
	
	@PostMapping("/signup")
	public String signupAction(
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
			try {
				applicationEventPublisher
				.publishEvent(new OnRegistrationCompleteEvent(
							user, 
							request.getContextPath()));
			}
			catch(Exception e) {
				return "redirect:/signup?email";
			}
		}
		catch(InvalidParameterException e) {
			return "redirect:/signup?registration";
		}
		return "authentication/confirm";
	}
	
	@PostMapping("/reset")
	public String resetPasswordAction() {
		return "redirect:/";
	}
}