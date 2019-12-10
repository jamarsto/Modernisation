package uk.me.jasonmarston.domain.entity;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import uk.me.jasonmarston.domain.aggregate.Account;
import uk.me.jasonmarston.domain.factory.entity.TransactionBuilderFactory;
import uk.me.jasonmarston.domain.value.Amount;
import uk.me.jasonmarston.domain.value.TransactionType;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.entity.AbstractEntity;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Configurable(autowire = Autowire.BY_TYPE)
@Entity(name = "TRANSACTIONS")
public class Transaction extends AbstractEntity {
	public static class Builder implements IBuilder<Transaction> {
		private TransactionType type;
		private Account account;
		private Amount amount;
		private EntityId referenceAccountId;

		private Builder() {
		}

		public Builder againstAccount(Account account) {
			this.account = account;
			return this;
		}

		@Override
		public Transaction build() {
			if(type == null || account == null || amount == null) {
				throw new RuntimeException("Invalid transaction.");
			}

			final Transaction transaction = new Transaction();
			transaction.type = type;
			transaction.account = account;
			transaction.ownerAccountId = account.getId();
			transaction.amount = amount;
			transaction.referenceAccountId = referenceAccountId;

			return transaction;
		}

		public Builder forAmount(Amount amount) {
			this.amount = amount;
			return this;
		}

		public Builder ofType(TransactionType type) {
			this.type = type;
			return this;
		}

		public Builder withReferenceAccountId(
				final EntityId referenceAccountId) {
			this.referenceAccountId = referenceAccountId;
			return this;
		}
	}

	@Service
	public static class FactoryImpl implements TransactionBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.ORDINAL)
	@NotNull
	private TransactionType type;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@AttributeOverride(name="id", column=@Column(name="ownerAccountId"))
	@NotNull
	private Account account;

	@JsonInclude
	@JsonUnwrapped
	@Transient
	@NotNull
	private EntityId ownerAccountId;

	@JsonUnwrapped
	@NotNull
	@Positive
	private Amount amount;

	@JsonUnwrapped
	@AttributeOverride(name="id", column=@Column(name="referenceAccountId"))
	private EntityId referenceAccountId;

	private Transaction() {
	}

	public Account getAccount() {
		return account;
	}

	public Amount getAmount() {
		return amount;
	}

	public EntityId getReferenceAccountId() {
		return referenceAccountId;
	}

	public TransactionType getType() {
		return type;
	}
}