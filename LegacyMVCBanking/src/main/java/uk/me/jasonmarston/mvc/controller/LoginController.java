package uk.me.jasonmarston.mvc.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	@GetMapping("/login")
	public ModelAndView login(
			final @RequestParam(value ="error", required = false) 
					String error,
			final @RequestParam(value = "logout", required = false)
					String logout,
			final HttpServletRequest request) {
		
		final ModelAndView model = new ModelAndView();
		if(error != null) {
			model.addObject("error", getErrorKey(request));
		}
		
		model.setViewName("login/index");
		
		return model;
	}

	private String getErrorKey(HttpServletRequest request) {
		final Exception e = (Exception)request
				.getSession()
				.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		if(e == null) {
			// this should never happen
			throw new 
				RuntimeException("Spring Framework Authentication Error");
		}

		if(e instanceof UsernameNotFoundException) {
			return "error.user.not.found";
		}

		if(e instanceof DisabledException) {
			return "error.user.disabled";
		}

		if(e instanceof LockedException) {
			return "error.user.locked";
		}

		if(e instanceof AccountExpiredException) {
			return "error.user.account.expired";
		}

		if(e instanceof CredentialsExpiredException) {
			return "error.user.credentials.expired";
		}

		if(e instanceof BadCredentialsException) {
			return "error.user.credentials.bad";
		}

		// this should never happen
		throw new RuntimeException(e.getMessage());
	}
}