package uk.me.jasonmarston.mvc.controller;

import static uk.me.jasonmarston.domain.Constants.STRONG_PASSWORD;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.mvc.controller.bean.ChangePasswordBean;

@Controller
@Validated
public class UserPasswordChangeController {
	@Autowired
	@Lazy
	private UserService userService;

	@GetMapping("/user/password/change")
	public String forgotten(final ModelMap model) {
		model.addAttribute("strongPassword", STRONG_PASSWORD);
		model.addAttribute("changePasswordBean", new ChangePasswordBean());
		return "user/password/change/index";
	}
	
	@PostMapping("/user/password/change")
	public String forgotten(
			@AuthenticationPrincipal User user,
			@ModelAttribute("changePasswordBean") 
					@NotNull @Valid final ChangePasswordBean
							changePasswordBean,
			final WebRequest request,
			final ModelMap model) {
		
		if(!userService.isCurrentPassword(user, 
				new Password(changePasswordBean.getCurrentPassword()))) {
			return "redirect:/user/password/change?incorrect";
		}

		user = userService.changePassword(user,
				new Password(changePasswordBean.getPassword()));
		
		AuthenticationHelper.loginUser(user);

		return "user/password/change/confirmation";
	}
}