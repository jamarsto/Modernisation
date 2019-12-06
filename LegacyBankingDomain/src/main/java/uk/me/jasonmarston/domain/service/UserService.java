package uk.me.jasonmarston.domain.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.aggregate.User;
import uk.me.jasonmarston.domain.details.EmailDetails;
import uk.me.jasonmarston.domain.details.RegistrationDetails;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface UserService {
	void delete(@NotNull @Valid final User user);
	User findByEmail(@NotNull @Valid final EmailDetails emailDetails);
	User getUser(@NotNull @Valid final EntityId id);
	User register(
			@NotNull @Valid final RegistrationDetails registrationDetails);
	User update(@NotNull @Valid final User user);
}