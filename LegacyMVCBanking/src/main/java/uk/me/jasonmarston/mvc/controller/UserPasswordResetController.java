package uk.me.jasonmarston.mvc.controller;

import static uk.me.jasonmarston.domain.Constants.STRONG_PASSWORD;

import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import uk.me.jasonmarston.domain.aggregate.ResetToken;
import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.service.ResetTokenService;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.domain.value.Token;
import uk.me.jasonmarston.mvc.alerts.AlertDanger;
import uk.me.jasonmarston.mvc.alerts.AlertInfo;
import uk.me.jasonmarston.mvc.controller.bean.ForgottenPasswordBean;
import uk.me.jasonmarston.mvc.controller.bean.ResetPasswordBean;
import uk.me.jasonmarston.mvc.event.OnPasswordResetEvent;

@Controller
@Validated
@SessionAttributes("token")
public class UserPasswordResetController {
	@Autowired
	@Lazy
	private ResetTokenService resetTokenService;

	@Autowired
	@Lazy
	private UserService userService;

	@Autowired
	@Lazy
	private ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	@Lazy
	private LocaleResolver localeResolver;

	@PostMapping("/user/password/reset")
	public ModelAndView forgotten(
			@ModelAttribute("forgottenPasswordBean") 
					@NotNull @Valid final ForgottenPasswordBean 
							forgottenPasswordBean,
			final HttpServletRequest request) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "resetPassword.heading");
		final AlertInfo alert = new AlertInfo("info.passwordResetEmail");
		model.addObject("alert", alert);

		applicationEventPublisher
				.publishEvent(new OnPasswordResetEvent(
						new EmailAddress(forgottenPasswordBean.getEmail()),
						request.getContextPath(),
						localeResolver.resolveLocale(request)));

		model.setViewName("confirmation");

		return model;
	}

	@GetMapping("/user/password/reset")
	public ModelAndView forgotten() {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "resetPassword.heading");
		model.addObject("forgottenPasswordBean", new ForgottenPasswordBean());
		
		model.setViewName("user/password/reset");

		return model;
	}

	@PostMapping("/user/password/reset/verification")
	public ModelAndView verification(
			@ModelAttribute("token") @NotNull @Valid final Token token,
			@ModelAttribute("resetPasswordBean") 
					@NotNull @Valid final ResetPasswordBean resetPasswordBean,
			final ModelMap modelSession) {
		modelSession.remove("token");

		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "resetPassword.heading");


		final ResetToken resetToken = resetTokenService
				.findByToken(token);
		if(resetToken == null) {
			return expiredViewAndModel(model);
		}

		resetTokenService.delete(resetToken.getId());

		if(resetToken.isExpired()) {
			return expiredViewAndModel(model);
		}

		User user = userService.findById(resetToken.getUserId());
		if(user == null) {
			return expiredViewAndModel(model);
		}

		try {
			user = userService.changePassword(user.getId(), 
					new Password(resetPasswordBean.getPassword()));
		}
		catch(OptimisticLockException e) {
			return expiredViewAndModel(model);
		}

		AuthenticationHelper.loginUser(user);

		final AlertInfo alert = new AlertInfo("info.passwordReset");
		model.addObject("alert", alert);
		
		model.setViewName("confirmation");

	    return model;
	}

	@GetMapping("/user/password/reset/verification")
	public ModelAndView verification(
			final WebRequest request, 
			@RequestParam(value = "token") final String tokenString) {
		final ModelAndView model = new ModelAndView();
		model.addObject("heading", "resetPassword.heading");
		model.addObject("strongPassword", STRONG_PASSWORD);

		final Token token =  new Token(tokenString);
		final ResetToken resetToken = 
				resetTokenService.findByToken(token);
		if(resetToken == null || resetToken.isExpired()) {
			return expiredViewAndModel(model);
		}

		final User user = userService.findById(resetToken.getUserId());
		if(user == null) {
			return expiredViewAndModel(model);
		}

		model.addObject("token", token);
		model.addObject("strongPassword", STRONG_PASSWORD);
		model.addObject("resetPasswordBean", new ResetPasswordBean());
		
		model.setViewName("user/password/reset/verification");

	    return model;
	}

	private ModelAndView expiredViewAndModel(final ModelAndView model) {
		model.addObject("forgottenPasswordBean", new ForgottenPasswordBean());
		final AlertDanger alert = new AlertDanger("error.token");
		model.addObject("alert", alert);
		
		model.setViewName("user/password/reset");
		
		return model;
	}

}