package uk.me.jasonmarston.domain.service.impl;

import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.details.EmailDetails;
import uk.me.jasonmarston.domain.details.RegistrationDetails;
import uk.me.jasonmarston.domain.factory.aggregate.UserBuilderFactory;
import uk.me.jasonmarston.domain.repository.UserRepository;
import uk.me.jasonmarston.domain.service.UserService;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Service
@Validated
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.READ_COMMITTED, 
		readOnly = false)
public class UserServiceImpl implements UserService {
	@Autowired
	@Lazy
	private UserBuilderFactory userBuilderFactory;

	@Autowired
	@Lazy
	private UserRepository userRepository;
	
	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;

	@Override
	public void delete(@NotNull @Valid User user) {
		userRepository.delete(user);
	}

	@Override
	public User findByEmail(@NotNull @Valid final EmailDetails emailDetails) {
		final Optional<User> optional = userRepository
				.findByEmail(emailDetails.getEmail());
		if(optional.isPresent()) {
			return optional.get();
		}

		return null;
	}

	@Override
	public User getUser(@NotNull @Valid final EntityId id) {
		final Optional<User> optional = userRepository.findById(id); 
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public User register(
			@NotNull @Valid final RegistrationDetails registrationDetails) {
		if(userRepository.findByEmail(registrationDetails
				.getEmail())
				.isPresent()) {
			throw new EntityExistsException("Already registered.");
		}

		final User.Builder builder = userBuilderFactory.create();

		final User user = builder
				.forEmail(registrationDetails.getEmail())
				.withPassword(passwordEncoder
						.encode(registrationDetails.getPassword()))
				.build();

		return userRepository.save(user);
	}

	@Override
	public User update(@NotNull @Valid final User user) {
		return userRepository.save(user);
	}
}