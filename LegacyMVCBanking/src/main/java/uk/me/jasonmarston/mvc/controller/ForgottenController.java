package uk.me.jasonmarston.mvc.controller;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import uk.me.jasonmarston.domain.details.TokenDetails;
import uk.me.jasonmarston.domain.service.ResetTokenService;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.mvc.controller.bean.ForgottenBean;
import uk.me.jasonmarston.mvc.controller.bean.ResetBean;
import uk.me.jasonmarston.mvc.event.OnForgottenPasswordEvent;

@Controller
@Validated
@SessionAttributes("token")
public class ForgottenController {
	
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
	private PasswordEncoder passwordEncoder;

	@GetMapping("/user/forgotten")
	public String forgotten(final ModelMap model) {
		model.addAttribute("forgottenBean", new ForgottenBean());
		return "user/forgotten/index";
	}
	
	@PostMapping("/user/forgotten")
	public String forgotten(
			@ModelAttribute("forgottenBean") 
					@NotNull @Valid final ForgottenBean forgottenBean,
			final WebRequest request,
			final ModelMap model) {
		applicationEventPublisher
				.publishEvent(new OnForgottenPasswordEvent(
						forgottenBean, 
						request.getContextPath()));

		return "user/forgotten/confirmation";
	}

	@GetMapping("/user/forgotten/verification")
	public String confirmation(
			final WebRequest request, 
			@RequestParam(value = "token") final String token,
			final ModelMap model) {
		if(StringUtils.isBlank(token)) {
			return "redirect:/user/forgotten?expired";
		}

		final ResetToken resetToken = 
				resetTokenService.findByToken(new TokenDetails(token));
		if(resetToken == null || resetToken.isExpired()) {
			return "redirect:/user/forgotten?expired";
		}

		final User user = userService.getUser(resetToken.getUserId());
		if(user == null) {
			return "redirect:/user/forgotten?expired";
		}

		model.addAttribute("token", token);
		model.addAttribute("resetBean", new ResetBean());

	    return "user/forgotten/verification/index";
	}

	@PostMapping("/user/forgotten/verification")
	public String reset(
			@ModelAttribute("token") @NotNull @NotBlank final String token,
			@ModelAttribute("resetBean") 
					@NotNull @Valid final ResetBean resetBean,
			final ModelMap model) {
		model.remove("token");

		if(StringUtils.isBlank(token)) {
			return "redirect:/user/forgotten?expired";
		}

		final ResetToken resetToken = resetTokenService
				.findByToken(new TokenDetails(token));
		if(resetToken == null) {
			return "redirect:/user/forgotten?expired";
		}

		resetTokenService.delete(resetToken);

		if(resetToken.isExpired()) {
			return "redirect:/user/forgotten?expired";
		}

		final User user = userService.getUser(resetToken.getUserId());
		if(user == null) {
			return "redirect:/user/forgotten?expired";
		}

		user.setPassword(passwordEncoder.encode(resetBean.getPassword()));

		try {
			userService.update(user);
		}
		catch(OptimisticLockException e) {
			return "redirect:/user/forgotten?expired";
		}

		AuthenticationHelper.loginUser(user);

	    return "user/forgotten/verification/confirmation";
	}
}