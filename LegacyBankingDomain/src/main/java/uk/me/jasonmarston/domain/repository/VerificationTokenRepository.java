package uk.me.jasonmarston.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
public interface VerificationTokenRepository extends CrudRepository<VerificationToken, EntityId> {
	Optional<VerificationToken> findByToken(final String token);
}
