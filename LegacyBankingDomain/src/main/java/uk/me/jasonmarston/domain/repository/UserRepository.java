package uk.me.jasonmarston.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.domain.aggregate.impl.User;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
public interface UserRepository extends CrudRepository<User, EntityId> {
	Optional<User> findByEmail(final String email);
}
