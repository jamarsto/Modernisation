package uk.me.jasonmarston.domain.entity.factory.impl;

import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.aggregate.impl.Account;
import uk.me.jasonmarston.domain.entity.factory.TransactionFactory;
import uk.me.jasonmarston.domain.entity.impl.Transaction;
import uk.me.jasonmarston.domain.type.impl.Amount;
import uk.me.jasonmarston.domain.type.impl.TransactionType;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Service
public class TransactionFactoryImpl implements TransactionFactory {

	@Override
	public Transaction create(final TransactionType type,
			final Account account,
			final Amount amount,
			final EntityId referenceAccountId) {
		return new Transaction(type, account, amount, referenceAccountId);
	}

}
