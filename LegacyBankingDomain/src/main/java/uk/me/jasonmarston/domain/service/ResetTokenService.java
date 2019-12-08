package uk.me.jasonmarston.domain.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.aggregate.ResetToken;
import uk.me.jasonmarston.domain.value.EmailAddress;
import uk.me.jasonmarston.domain.value.Token;

public interface ResetTokenService {
	ResetToken create(@NotNull @Valid final EmailAddress email);
	void delete(@NotNull @Valid final ResetToken ResetToken);
	ResetToken findByToken(@NotNull @Valid final Token token);
	List<ResetToken> findExpiredTokens();
}