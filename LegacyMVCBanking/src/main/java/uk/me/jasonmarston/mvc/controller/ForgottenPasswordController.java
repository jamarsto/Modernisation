package uk.me.jasonmarston.mvc.controller;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import uk.me.jasonmarston.domain.aggregate.ResetToken;
import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.service.ResetTokenService;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.domain.value.Token;
import uk.me.jasonmarston.mvc.controller.bean.ForgottenPasswordBean;
import uk.me.jasonmarston.mvc.controller.bean.ResetPasswordBean;
import uk.me.jasonmarston.mvc.event.OnForgottenPasswordEvent;

@Controller
@Validated
@SessionAttributes("token")
public class ForgottenPasswordController {
	
	@Autowired
	@Lazy
	private ResetTokenService resetTokenService;
	
	@Autowired
	@Lazy
	private UserService userService;
	
	@Autowired
	@Lazy
	private ApplicationEventPublisher applicationEventPublisher;

	@PostMapping("/user/forgotten")
	public String forgotten(
			@ModelAttribute("forgottenBean") 
					@NotNull @Valid final ForgottenPasswordBean forgottenBean,
			final WebRequest request,
			final ModelMap model) {
		applicationEventPublisher
				.publishEvent(new OnForgottenPasswordEvent(
						new EmailAddress(forgottenBean.getEmail()), 
						request.getContextPath()));

		return "user/forgotten/confirmation";
	}
	
	@GetMapping("/user/forgotten")
	public String forgotten(final ModelMap model) {
		model.addAttribute("forgottenBean", new ForgottenPasswordBean());
		return "user/forgotten/index";
	}

	@PostMapping("/user/forgotten/verification")
	public String verification(
			@ModelAttribute("token") @NotNull @Valid final Token token,
			@ModelAttribute("resetBean") 
					@NotNull @Valid final ResetPasswordBean resetBean,
			final ModelMap model) {
		model.remove("token");

		final ResetToken resetToken = resetTokenService
				.findByToken(token);
		if(resetToken == null) {
			return "redirect:/user/forgotten?expired";
		}

		resetTokenService.delete(resetToken);

		if(resetToken.isExpired()) {
			return "redirect:/user/forgotten?expired";
		}

		User user = userService.findById(resetToken.getUserId());
		if(user == null) {
			return "redirect:/user/forgotten?expired";
		}

		try {
			user = userService.changePassword(user, 
					new Password(resetBean.getPassword()));
		}
		catch(OptimisticLockException e) {
			return "redirect:/user/forgotten?expired";
		}

		AuthenticationHelper.loginUser(user);

	    return "user/forgotten/verification/confirmation";
	}

	@GetMapping("/user/forgotten/verification")
	public String verification(
			final WebRequest request, 
			@RequestParam(value = "token") final String tokenString,
			final ModelMap model) {
		final Token token =  new Token(tokenString);
		final ResetToken resetToken = 
				resetTokenService.findByToken(token);
		if(resetToken == null || resetToken.isExpired()) {
			return "redirect:/user/forgotten?expired";
		}

		final User user = userService.findById(resetToken.getUserId());
		if(user == null) {
			return "redirect:/user/forgotten?expired";
		}

		model.addAttribute("token", token);
		model.addAttribute("resetBean", new ResetPasswordBean());

	    return "user/forgotten/verification/index";
	}
}