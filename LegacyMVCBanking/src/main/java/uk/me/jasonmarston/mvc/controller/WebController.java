package uk.me.jasonmarston.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
	@GetMapping("/error")
	public String error(final Model model) {
		return "error";
	}
	
	@GetMapping("/")
	public String home(final Model model) {
		return "index";
	}
	
	@GetMapping("/login")
	public String signinAction(final Model model) {
		return "login";
	}
}