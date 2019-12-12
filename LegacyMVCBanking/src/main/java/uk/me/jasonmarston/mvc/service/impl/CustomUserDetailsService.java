package uk.me.jasonmarston.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.domain.value.EmailAddress;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	@Lazy
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(final String email)
			throws UsernameNotFoundException {
		if(email == null) {
			throw new UsernameNotFoundException("No Email Provided");
		}

		final User user = userService.findByEmail(new EmailAddress(email));
		if(user == null) {
			throw new UsernameNotFoundException(
					"User not found with email: " + email);
		}

        return user;
	}
}