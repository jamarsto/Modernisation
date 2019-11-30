package uk.me.jasonmarston.domain.service;

import java.util.List;

import uk.me.jasonmarston.domain.aggregate.impl.VerificationToken;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface VerificationTokenService {
	VerificationToken findByToken(final String token);
	VerificationToken create(final EntityId id);
	void delete(final VerificationToken verificationToken);
	List<VerificationToken> findExpiredTokens();
}