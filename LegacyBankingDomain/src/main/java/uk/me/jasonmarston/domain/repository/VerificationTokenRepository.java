package uk.me.jasonmarston.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
public interface VerificationTokenRepository extends 
		CrudRepository<VerificationToken, EntityId>,
		JpaSpecificationExecutor<VerificationToken> {
	Optional<VerificationToken> findByToken(final String token);
}
