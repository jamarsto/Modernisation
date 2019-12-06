package uk.me.jasonmarston.mvc.controller;

import javax.persistence.EntityExistsException;
import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.aggregate.VerificationToken;
import uk.me.jasonmarston.domain.details.TokenDetails;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.mvc.controller.bean.RegistrationBean;
import uk.me.jasonmarston.mvc.event.OnRegistrationCompleteEvent;

@Controller
@Validated
public class RegistrationController {
	@Autowired
	@Lazy
	private VerificationTokenService verificationTokenService;
	
	@Autowired
	@Lazy
	private UserService userService;
	
	@Autowired
	@Lazy
	private ApplicationEventPublisher applicationEventPublisher;

	@GetMapping("/user/registration")
	public String registration(final ModelMap model) {
		model.addAttribute("registrationBean", new RegistrationBean());
		return "user/registration/index";
	}

	@PostMapping("/user/registration")
	public String registration(
			@ModelAttribute("registrationBean") 
					@NotNull @Valid final RegistrationBean registrationBean,
			final WebRequest request) {
		try {
			final User user = userService.register(registrationBean);

			applicationEventPublisher
					.publishEvent(new OnRegistrationCompleteEvent(
							user, 
							request.getContextPath()));
		}
		catch(final EntityExistsException e) {
			return "redirect:/user/registration?registration";
		}

		return "user/registration/confirmation";
	}

	@GetMapping("/user/registration/verification")
	public String confirmation(
			@RequestParam(value = "token") final String token) {
		if(StringUtils.isBlank(token)) {
			return "redirect:/user/registration?expired";
		}

		final VerificationToken verificationToken = 
				verificationTokenService.findByToken(new TokenDetails(token));

		if(verificationToken == null) {
			return "redirect:/user/registration?expired";
		}

		final User user = userService.getUser(verificationToken.getUserId());
		if(user == null) {
			return "redirect:/user/registration?expired";
		}

		if(verificationToken.isExpired()) {
			verificationTokenService.delete(verificationToken);
			return "redirect:/user/registration?expired";
		}

		user.setEnabled(true);

		try {
			userService.update(user);
		}
		catch(OptimisticLockException e) {
			return "redirect:/user/registration?expired";
		}

		verificationTokenService.delete(verificationToken);

		AuthenticationHelper.loginUser(user);
	    
		return "user/registration/verification/confirmation";
	}
}