package uk.me.jasonmarston.domain.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import uk.me.jasonmarston.domain.entity.Transaction;
import uk.me.jasonmarston.domain.factory.aggregate.AccountBuilderFactory;
import uk.me.jasonmarston.domain.factory.entity.TransactionBuilderFactory;
import uk.me.jasonmarston.domain.value.Amount;
import uk.me.jasonmarston.domain.value.Balance;
import uk.me.jasonmarston.domain.value.TransactionType;
import uk.me.jasonmarston.framework.domain.aggregate.AbstractAggregate;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Entity
@Table(name = "ACCOUNTS")
public class Account extends AbstractAggregate {
	public static class Builder implements IBuilder<Account> {
		private Balance balance;

		private Builder() {
		}

		@Override
		public Account build() {
			if(balance == null) {
				throw new IllegalArgumentException("An opening balance is required");
			}

			final Account account = new Account();
			account.balance = balance;

			return account;
		}

		public Builder withOpeningBalance(Balance balance) {
			this.balance = balance;
			return this;
		}
	}

	@Service
	public static class Factory implements AccountBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@JsonUnwrapped
	@NotNull
	private Balance balance;

	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "account", fetch = FetchType.EAGER)
	@NotNull
	private List<@NotNull Transaction> transactions = 
			new ArrayList<Transaction>();

	private Account() {
		super();
	}

	public Transaction depositFunds(final Amount amount) {
		return depositFunds(amount, null);
	}

	public Transaction depositFunds(final Amount amount, 
			final EntityId referenceAccountId) {
		balance = balance.add(amount);

		final Transaction.Builder builder = 
				getBean(TransactionBuilderFactory.class).create();
		final Transaction transaction = builder
				.againstAccount(this)
				.ofType(TransactionType.DEPOSIT)
				.forAmount(amount)
				.withReferenceAccountId(referenceAccountId)
				.build();

		transactions.add(transaction);

		return transaction;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj, "transactions");
	}

	public Balance getBalance() {
		return balance;
	}

	@JsonIgnore
	public List<Transaction> getDeposits() {
		return transactions
			.stream()
			.filter(transaction -> 
				TransactionType.DEPOSIT.equals(transaction.getType()))
			.collect(Collectors.toList());
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	@JsonIgnore
	public List<Transaction> getWithdrawals() {
		return transactions
			.stream()
			.filter(transaction -> 
				TransactionType.WITHDRAWAL.equals(transaction.getType()))
			.collect(Collectors.toList());
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, "transactions");
	}

	public Transaction withdrawFunds(final Amount amount) {
		return withdrawFunds(amount, null);
	}

	public Transaction withdrawFunds(final Amount amount,
			final EntityId referenceAccountId) {
		balance = balance.subtract(amount);

		final Transaction.Builder builder = 
				getBean(TransactionBuilderFactory.class).create();
		final Transaction transaction = builder
				.againstAccount(this)
				.ofType(TransactionType.WITHDRAWAL)
				.forAmount(amount)
				.withReferenceAccountId(referenceAccountId)
				.build();

		transactions.add(transaction);

		return transaction;
	}
}