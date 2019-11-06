package uk.me.jasonmarston.legacy.domain.entity.factory;

import uk.me.jasonmarston.legacy.domain.aggregate.impl.Account;
import uk.me.jasonmarston.legacy.domain.entity.impl.Transaction;
import uk.me.jasonmarston.legacy.domain.type.impl.Amount;
import uk.me.jasonmarston.legacy.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.domain.type.impl.TransactionType;

public interface TransactionFactory {
	Transaction create(final TransactionType type, final Account account, final Amount amount, final EntityId referenceAccountId);
}
