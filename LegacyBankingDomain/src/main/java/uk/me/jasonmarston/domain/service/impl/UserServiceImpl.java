package uk.me.jasonmarston.domain.service.impl;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.domain.repository.UserRepository;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Service
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.READ_COMMITTED, 
		readOnly = false)
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User findByEmail(final String email) {
		final Optional<User> optional = userRepository.findByEmail(email); 
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public User getUser(final EntityId id) {
		final Optional<User> optional = userRepository.findById(id); 
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public User signUp(final String email,
			final String password,
			final String passwordConfirmation) {
		if(StringUtils.isBlank(password)) {
			if(StringUtils.isBlank(passwordConfirmation)) {
				throw new InvalidParameterException("No passwords Provided");
			}
			throw new InvalidParameterException("Passwords are not the same");
		}
		else if(!password.equals(passwordConfirmation)) {
			throw new InvalidParameterException("Passwords are not the same");
		}
		if(userRepository.findByEmail(email).isPresent()) {
			throw new InvalidParameterException("Already registered.");
		}
		
		final User user = new User(email, passwordEncoder.encode(password));
		user.setEnabled(false);
		return userRepository.save(user);
	}

	@Override
	public User create(final String uid,
			final String email,
			final String username,
			final String picture) {
		return userRepository.save(new User(uid, email, username,  picture));
	}

	@Override
	public User update(User user) {
		return userRepository.save(user);
	}

	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}
}