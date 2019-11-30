package uk.me.jasonmarston.domain.service;

import java.util.List;

import uk.me.jasonmarston.domain.aggregate.impl.ResetToken;

public interface ResetTokenService {
	ResetToken findByToken(final String token);
	ResetToken create(final String email);
	void delete(final ResetToken ResetToken);
	List<ResetToken> findExpiredTokens();
}