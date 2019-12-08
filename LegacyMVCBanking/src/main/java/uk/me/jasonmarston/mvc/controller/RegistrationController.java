package uk.me.jasonmarston.mvc.controller;

import javax.persistence.EntityExistsException;
import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import uk.me.jasonmarston.domain.details.RegistrationDetails;
import uk.me.jasonmarston.domain.factory.details.DetailsBuilderFactory;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.service.VerificationTokenService;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.domain.value.Token;
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
	
	@Autowired
	@Lazy
	private DetailsBuilderFactory factory;

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
			final RegistrationDetails.Builder builder = 
					factory.create(RegistrationDetails.Builder.class);

			final RegistrationDetails registrationDetails = builder
					.forEmail(new EmailAddress(registrationBean.getEmail()))
					.withPassword(new Password(registrationBean.getPassword()))
					.andPasswordConfirmation(
							new Password(
									registrationBean.getPasswordConfirmation()
							)
					)
					.build();

			final User user = userService.register(registrationDetails);

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
	public String verification(
			@RequestParam(value = "token") final String tokenString) {
		final Token token = new Token(tokenString);
		final VerificationToken verificationToken = 
				verificationTokenService.findByToken(token);
		if(verificationToken == null) {
			return "redirect:/user/registration?expired";
		}

		verificationTokenService.delete(verificationToken);

		if(verificationToken.isExpired()) {
			return "redirect:/user/registration?expired";
		}

		User user = userService.findById(verificationToken.getUserId());
		if(user == null) {
			return "redirect:/user/registration?expired";
		}

		try {
			user = userService.enable(user);
		}
		catch(OptimisticLockException e) {
			return "redirect:/user/registration?expired";
		}

		AuthenticationHelper.loginUser(user);
	    
		return "user/registration/verification/confirmation";
	}
}