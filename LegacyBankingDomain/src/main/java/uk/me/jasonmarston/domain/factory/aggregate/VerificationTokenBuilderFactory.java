package uk.me.jasonmarston.domain.factory.aggregate;

import uk.me.jasonmarston.domain.aggregate.VerificationToken;

public interface VerificationTokenBuilderFactory {
	VerificationToken.Builder create();
}
