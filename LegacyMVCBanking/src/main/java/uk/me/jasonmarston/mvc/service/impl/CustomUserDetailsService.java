package uk.me.jasonmarston.mvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.service.UserService;

@Service
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.READ_COMMITTED, 
		readOnly = false)
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(final String email)
			throws UsernameNotFoundException {
		final User user = userService.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException(
					"User not found with email: " + email);
		}
        return user;
	}
}