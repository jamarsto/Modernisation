package uk.me.jasonmarston.legacy.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import uk.me.jasonmarston.legacy.domain.entity.impl.Transaction;
import uk.me.jasonmarston.legacy.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.domain.type.impl.TransactionType;

public class TransactionSpecifications {
	public static class AccountIdAndTypeAndIdSpec 
			implements Specification<Transaction> {
		private static final long serialVersionUID = 1L;
		private EntityId accountId;
		private TransactionType transactionType;
		private EntityId id;
		
		public AccountIdAndTypeAndIdSpec(
				final EntityId accountId,
				final TransactionType transactionType,
				final EntityId id) {
			this.accountId = accountId;
			this.transactionType = transactionType;
			this.id = id;
		}

		@Override
		public Predicate toPredicate(
				Root<Transaction> root, 
				CriteriaQuery<?> query, 
				CriteriaBuilder criteriaBuilder) {
			return criteriaBuilder
				.and(
					criteriaBuilder.and(
						criteriaBuilder.equal(
							root.get("account").get("id"), this.accountId),
						criteriaBuilder.equal(root.get("type"), transactionType)),
					criteriaBuilder.equal(root.get("id"), this.id)
				);
		}
	}

	public static class AccountIdAndIdSpec 
			implements Specification<Transaction> {

		private static final long serialVersionUID = 1L;
		private EntityId accountId;
		private EntityId id;

		public AccountIdAndIdSpec(
				final EntityId accountId,
				final EntityId id) {
			this.accountId = accountId;
			this.id = id;
		}

		@Override
		public Predicate toPredicate(
				Root<Transaction> root, 
				CriteriaQuery<?> query, 
				CriteriaBuilder criteriaBuilder) {
			return criteriaBuilder
				.and(
					criteriaBuilder.equal(
						root.get("account").get("id"), this.accountId),
					criteriaBuilder.equal(root.get("id"), this.id)
				);
		}
	}
}
