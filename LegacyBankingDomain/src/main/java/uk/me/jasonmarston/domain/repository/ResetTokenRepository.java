package uk.me.jasonmarston.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.domain.aggregate.impl.ResetToken;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
public interface ResetTokenRepository extends 
		CrudRepository<ResetToken, EntityId>,
		JpaSpecificationExecutor<ResetToken> {
	Optional<ResetToken> findByToken(final String token);
}
