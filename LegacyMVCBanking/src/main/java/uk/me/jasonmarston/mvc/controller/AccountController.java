package uk.me.jasonmarston.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Validated
public class AccountController {
	
	@GetMapping("/")
	public String home() {
		return "account/index";
	}
}