package uk.me.jasonmarston.domain.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.aggregate.ResetToken;
import uk.me.jasonmarston.domain.details.EmailDetails;
import uk.me.jasonmarston.domain.details.TokenDetails;

public interface ResetTokenService {
	ResetToken create(@NotNull @Valid final EmailDetails emailDetails);
	void delete(@NotNull @Valid final ResetToken ResetToken);
	ResetToken findByToken(@NotNull @Valid final TokenDetails tokenDetails);
	List<ResetToken> findExpiredTokens();
}