package uk.me.jasonmarston.domain.factory.entity;

import uk.me.jasonmarston.domain.entity.Transaction;

public interface TransactionBuilderFactory {
	Transaction.Builder create();
}
