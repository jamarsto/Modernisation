package uk.me.jasonmarston.domain.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.details.RegistrationDetails;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Password;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface UserService {
	User changePassword(
			@NotNull @Valid final User user,
			@NotNull @Valid final Password password);
	void delete(@NotNull @Valid final User user);
	User enable(@NotNull @Valid final User user);
	User findByEmail(@NotNull @Valid final EmailAddress email);
	User findById(@NotNull @Valid final EntityId id);
	User register(
			@NotNull @Valid final RegistrationDetails registrationDetails);
}