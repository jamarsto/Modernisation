package uk.me.jasonmarston.legacy.domain.aggregate.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import uk.me.jasonmarston.framework.domain.aggregate.AbstractAggregate;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.domain.entity.factory.TransactionFactory;
import uk.me.jasonmarston.legacy.domain.entity.impl.Transaction;
import uk.me.jasonmarston.legacy.domain.type.impl.Amount;
import uk.me.jasonmarston.legacy.domain.type.impl.TransactionType;

@Entity
@Table(name = "ACCOUNTS")
public class Account extends AbstractAggregate {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Transient
	private TransactionFactory transactionFactory;

	@JsonUnwrapped
	private Amount balance;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL)
	private List<Transaction> transactions = new ArrayList<Transaction>();
	
	public Account() {
		balance = new Amount(new BigDecimal("0.00"));
		this.setId(new EntityId());
	}

	public Amount getBalance() {
		return balance;
	}
	
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	@JsonIgnore
	public List<Transaction> getDeposits() {
		return transactions
			.stream()
			.filter(transaction -> 
				TransactionType.DEPOSIT.equals(transaction.getType()))
			.collect(Collectors.toList());
	}
	
	@JsonIgnore
	public List<Transaction> getWithdrawals() {
		return transactions
			.stream()
			.filter(transaction -> 
				TransactionType.WITHRAWAL.equals(transaction.getType()))
			.collect(Collectors.toList());
	}

	public Transaction depositFunds(final Amount amount) {
		return depositFunds(amount, null);
	}

	public Transaction depositFunds(final Amount amount, 
			final EntityId referenceAccountId) {
		this.balance = this.balance.add(amount);
		final Transaction transaction = 
			transactionFactory.create(
				TransactionType.DEPOSIT,
				this, 
				amount,
				referenceAccountId);
		transactions.add(transaction);
		return transaction;
	}

	public Transaction withdrawFunds(final Amount amount) {
		return withdrawFunds(amount, null);
	}

	public Transaction withdrawFunds(final Amount amount,
			final EntityId referenceAccountId) {
		this.balance = this.balance.subtract(amount);
		Transaction transaction = 
			transactionFactory.create(
				TransactionType.WITHRAWAL,
				this, 
				amount,
				referenceAccountId);
		transactions.add(transaction);
		return transaction;
	}
}
