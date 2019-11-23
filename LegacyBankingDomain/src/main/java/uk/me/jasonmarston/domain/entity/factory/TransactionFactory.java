package uk.me.jasonmarston.domain.entity.factory;

import uk.me.jasonmarston.domain.aggregate.impl.Account;
import uk.me.jasonmarston.domain.entity.impl.Transaction;
import uk.me.jasonmarston.domain.type.impl.Amount;
import uk.me.jasonmarston.domain.type.impl.TransactionType;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public interface TransactionFactory {
	Transaction create(final TransactionType type,
			final Account account,
			final Amount amount,
			final EntityId referenceAccountId);
}
