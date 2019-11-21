package uk.me.jasonmarston.legacy.domain.service;

import java.util.List;

import uk.me.jasonmarston.framework.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.domain.aggregate.impl.Account;
import uk.me.jasonmarston.legacy.domain.entity.impl.Transaction;
import uk.me.jasonmarston.legacy.domain.type.impl.Amount;

public interface AccountService {
	Account openAccount();
	Account getAccount(final EntityId id);
	Transaction depositFunds(final EntityId id, final Amount amount);
	Transaction withdrawFunds(final EntityId id, final Amount amount);
	Amount getBalance(final EntityId id);
	Transaction getDeposit(final EntityId accountId,
			final EntityId transactionId);
	List<Transaction> getDeposits(final EntityId accountId);
	Transaction getTransaction(final EntityId accountId,
			final EntityId transactionId);
	List<Transaction> getTransactions(final EntityId accountId);
	Transaction getWithdrawal(final EntityId accountId,
			final EntityId transactionId);
	List<Transaction> getWithdrawals(final EntityId accountId);
}
