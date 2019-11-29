package uk.me.jasonmarston.domain.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import uk.me.jasonmarston.domain.aggregate.factory.AccountFactory;
import uk.me.jasonmarston.domain.aggregate.impl.Account;
import uk.me.jasonmarston.domain.entity.impl.Transaction;
import uk.me.jasonmarston.domain.repository.AccountRepository;
import uk.me.jasonmarston.domain.repository.TransactionRepository;
import uk.me.jasonmarston.domain.repository.specification.impl.TransactionSpecification.DepositHasIdAndAccountId;
import uk.me.jasonmarston.domain.repository.specification.impl.TransactionSpecification.TransactionHasIdAndAccountId;
import uk.me.jasonmarston.domain.repository.specification.impl.TransactionSpecification.WithdrawalHasIdAndAccountId;
import uk.me.jasonmarston.domain.service.AccountService;
import uk.me.jasonmarston.domain.type.impl.Amount;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Service
@Transactional(propagation = Propagation.REQUIRED, 
		isolation = Isolation.READ_COMMITTED, 
		readOnly = false)
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountFactory accountFactory;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	public Transaction depositFunds(final EntityId id,
			final Amount amount) {
		final Optional<Account> optional = accountRepository.findById(id);
		if(optional.isPresent()) {
			final Account account = optional.get();
			final Transaction transaction = account.depositFunds(amount);
			accountRepository.save(account);
			return transaction;
		}
		return null;
	}

	@Override
	public Account getAccount(final EntityId id) {
		final Optional<Account> optional = accountRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,
			isolation = Isolation.REPEATABLE_READ,
			readOnly = true)
	public Amount getBalance(final EntityId id) {
		final Optional<Account> optional = accountRepository.findById(id);
		if(optional.isPresent()) {
			final Account account = optional.get();
			return account.getBalance();
		}
		return null;
	}

	@Override
	public Transaction getDeposit(
			final EntityId accountId,
			final EntityId id) {
		final Optional<Transaction> optional = transactionRepository
				.findOne(new DepositHasIdAndAccountId(id, accountId));
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<Transaction> getDeposits(final EntityId accountId) {
		final Optional<Account> optional = accountRepository.findById(accountId);
		if(optional.isPresent()) {
			return optional.get().getDeposits();
		}
		return new ArrayList<Transaction>();
	}

	@Override
	public Transaction getTransaction(
			final EntityId accountId,
			final EntityId id) {
		final Optional<Transaction> optional = transactionRepository
				.findOne(new TransactionHasIdAndAccountId(id, accountId));
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<Transaction> getTransactions(final EntityId accountId) {
		final Optional<Account> optional = accountRepository.findById(accountId);
		if(optional.isPresent()) {
			return optional.get().getTransactions();
		}
		return null;
	}

	@Override
	public Transaction getWithdrawal(
			final EntityId accountId,
			final EntityId id) {
		final Optional<Transaction> optional = transactionRepository
				.findOne(new WithdrawalHasIdAndAccountId(id, accountId));
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public List<Transaction> getWithdrawals(final EntityId accountId) {
		final Optional<Account> optional = accountRepository.findById(accountId);
		if(optional.isPresent()) {
			return optional.get().getWithdrawals();
		}
		return new ArrayList<Transaction>();
	}

	@Override
	public Account openAccount() {
		return accountRepository.save(accountFactory.create());
	}

	@Override
	public Transaction withdrawFunds(final EntityId id, final Amount amount) {
		final Optional<Account> optional = accountRepository.findById(id);
		if(optional.isPresent()) {
			final Account account = optional.get();
			final Transaction transaction = account.withdrawFunds(amount);
			accountRepository.save(account);
			return transaction;
		}
		return null;
	}
}