package uk.me.jasonmarston.legacy.repository.specification.impl;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import uk.me.jasonmarston.legacy.domain.entity.impl.Transaction;
import uk.me.jasonmarston.legacy.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.domain.type.impl.TransactionType;

public class TransactionSpecification {
	public static class HasIdAccountIdAndType 
			implements Specification<Transaction> {
		private static final long serialVersionUID = 1L;
		private EntityId id;
		private EntityId accountId;
		private TransactionType transactionType;
		
		public HasIdAccountIdAndType(
				final EntityId id,
				final EntityId accountId,
				final TransactionType transactionType) {
			this.id = id;
			this.accountId = accountId;
			this.transactionType = transactionType;
		}
		
		@Override
		public Predicate toPredicate(
				final Root<Transaction> root, 
				final CriteriaQuery<?> query, 
				final CriteriaBuilder builder) {
			return builder.and(
					id(root, builder),
					builder.and(
						accountId(root, builder),
						transactionType(root, builder)));
		}

		private Predicate accountId(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("account").get("id"),
					this.accountId);			
		}
		
		private Predicate id(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("id"), this.id);
		}
		
		private Predicate transactionType(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("type"), transactionType);
		}
	}

	public static class HasIdAndAccountId 
			implements Specification<Transaction> {

		private static final long serialVersionUID = 1L;
		private EntityId id;
		private EntityId accountId;

		public HasIdAndAccountId(
				final EntityId id,
				final EntityId accountId) {
			this.id = id;
			this.accountId = accountId;
		}
		
		@Override
		public Predicate toPredicate(
				final Root<Transaction> root, 
				final CriteriaQuery<?> query, 
				final CriteriaBuilder builder) {
			return builder.and(id(root, builder), accountId(root, builder));
		}

		private Predicate accountId(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("id"), this.accountId);
		}
		
		private Predicate id(final Root<Transaction> root,
				final CriteriaBuilder builder) {
			return builder.equal(root.get("id"), this.id);
		}
	}
	
	public static class WithdrawalHasIdAndAccountId 
		implements Specification<Transaction> {

		private static final long serialVersionUID = 1L;
		private EntityId id;
		private EntityId accountId;

		public WithdrawalHasIdAndAccountId(
				final EntityId id,
				final EntityId accountId) {
			this.id = id;
			this.accountId = accountId;
		}

		@Override
		public Predicate toPredicate(final Root<Transaction> root,
				final CriteriaQuery<?> query,
				final CriteriaBuilder builder) {
			return new TransactionSpecification
					.HasIdAccountIdAndType(id,
							accountId,
							TransactionType.WITHRAWAL)
					.toPredicate(root, query, builder);
		}
	}

	public static class DepositHasIdAndAccountId 
		implements Specification<Transaction> {

		private static final long serialVersionUID = 1L;
		private EntityId id;
		private EntityId accountId;

		public DepositHasIdAndAccountId(
				final EntityId id,
				final EntityId accountId) {
			this.accountId = accountId;
			this.id = id;
		}

		@Override
		public Predicate toPredicate(final Root<Transaction> root,
				final CriteriaQuery<?> query,
				final CriteriaBuilder builder) {
			return new TransactionSpecification
					.HasIdAccountIdAndType(id,
							accountId,
							TransactionType.DEPOSIT)
					.toPredicate(root, query, builder);
		}
	}
}
