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

import uk.me.jasonmarston.domain.aggregate.ResetToken;
import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.service.ResetTokenService;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.domain.value.Token;
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
	public String forgotten(
			@ModelAttribute("forgottenPasswordBean") 
					@NotNull @Valid final ForgottenPasswordBean 
							forgottenPasswordBean,
			final HttpServletRequest request,
			final ModelMap model) {
		applicationEventPublisher
				.publishEvent(new OnPasswordResetEvent(
						new EmailAddress(forgottenPasswordBean.getEmail()),
						request.getContextPath(),
						localeResolver.resolveLocale(request)));

		return "user/password/reset/confirmation";
	}

	@GetMapping("/user/password/reset")
	public String forgotten(final ModelMap model) {
		model.addAttribute("forgottenPasswordBean", 
				new ForgottenPasswordBean());

		return "user/password/reset/index";
	}

	@PostMapping("/user/password/reset/verification")
	public String verification(
			@ModelAttribute("token") @NotNull @Valid final Token token,
			@ModelAttribute("resetPasswordBean") 
					@NotNull @Valid final ResetPasswordBean resetPasswordBean,
			final ModelMap model) {
		model.remove("token");

		final ResetToken resetToken = resetTokenService
				.findByToken(token);
		if(resetToken == null) {
			return "redirect:/user/password/reset?expired";
		}

		resetTokenService.delete(resetToken);

		if(resetToken.isExpired()) {
			return "redirect:/user/password/reset?expired";
		}

		User user = userService.findById(resetToken.getUserId());
		if(user == null) {
			return "redirect:/user/password/reset?expired";
		}

		try {
			user = userService.changePassword(user, 
					new Password(resetPasswordBean.getPassword()));
		}
		catch(OptimisticLockException e) {
			return "redirect:/user/password/reset?expired";
		}

		AuthenticationHelper.loginUser(user);

	    return "redirect:/user/password/reset/verification/confirmation";
	}

	@GetMapping("/user/password/reset/verification")
	public String verification(
			final WebRequest request, 
			@RequestParam(value = "token") final String tokenString,
			final ModelMap model) {
		final Token token =  new Token(tokenString);
		final ResetToken resetToken = 
				resetTokenService.findByToken(token);
		if(resetToken == null || resetToken.isExpired()) {
			return "redirect:/user/password/reset?expired";
		}

		final User user = userService.findById(resetToken.getUserId());
		if(user == null) {
			return "redirect:/user/password/reset?expired";
		}

		model.addAttribute("token", token);
		model.addAttribute("strongPassword", STRONG_PASSWORD);
		model.addAttribute("resetPasswordBean", new ResetPasswordBean());

	    return "user/password/reset/verification/index";
	}

	@GetMapping("/user/password/reset/verification/confirmation")
	public String verificationConfirmation() {
		return "user/password/reset/verification/confirmation";
	}
}