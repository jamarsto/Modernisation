package uk.me.jasonmarston.legacy.domain.service;

import uk.me.jasonmarston.framework.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.domain.type.impl.Amount;

public interface TransferService {
	void transferFunds(final EntityId fromAccount,
			final EntityId toAccount,
			final Amount amount);
}
