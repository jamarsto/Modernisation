package uk.me.jasonmarston.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
	
	@GetMapping("/")
	public String home(final Model model) {
		return "account/index";
	}
}
