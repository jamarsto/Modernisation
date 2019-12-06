package uk.me.jasonmarston.mvc.controller;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import uk.me.jasonmarston.domain.aggregate.User;

class AuthenticationHelper {
	private AuthenticationHelper() {
	}

	public static void loginUser(final User user) {
		final Authentication authentication = 
				new UsernamePasswordAuthenticationToken(
						user,
						user.getPassword(),
						user.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}