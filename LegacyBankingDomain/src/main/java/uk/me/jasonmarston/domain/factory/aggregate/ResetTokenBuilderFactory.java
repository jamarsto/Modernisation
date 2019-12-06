package uk.me.jasonmarston.domain.factory.aggregate;

import uk.me.jasonmarston.domain.aggregate.ResetToken;

public interface ResetTokenBuilderFactory {
	ResetToken.Builder create();
}
