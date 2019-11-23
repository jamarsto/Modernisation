package uk.me.jasonmarston.domain.service;

import uk.me.jasonmarston.domain.type.impl.Amount;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface TransferService {
	void transferFunds(final EntityId fromAccount,
			final EntityId toAccount,
			final Amount amount);
}
