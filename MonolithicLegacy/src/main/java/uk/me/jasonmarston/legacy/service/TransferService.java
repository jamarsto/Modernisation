package uk.me.jasonmarston.legacy.service;

import uk.me.jasonmarston.legacy.domain.type.impl.Amount;
import uk.me.jasonmarston.legacy.domain.type.impl.EntityId;

public interface TransferService {
	void transferFunds(final EntityId fromAccount,
			final EntityId toAccount,
			final Amount amount);
}
