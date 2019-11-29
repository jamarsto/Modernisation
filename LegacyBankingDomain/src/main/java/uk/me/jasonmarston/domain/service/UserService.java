package uk.me.jasonmarston.domain.service;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface UserService {
	User findByEmail(final String email);
	User getUser(final EntityId id);
	User signUp(final String email,
			final String password,
			final String passwordConfirmation);
	User create(final String uid,
				final String email,
				final String username,
				final String picture);
	User update(final User user);
}