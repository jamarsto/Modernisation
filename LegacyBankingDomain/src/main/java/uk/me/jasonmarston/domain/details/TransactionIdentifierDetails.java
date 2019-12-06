package uk.me.jasonmarston.domain.details;

import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class TransactionIdentifierDetails extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Account Id is required")
	private EntityId accountId;
	
	@NotNull(message = "Transaction Id is required")
	private EntityId transactionId;

	public TransactionIdentifierDetails(
			final EntityId accountId,
			final EntityId transactionId) {
		this.accountId = accountId;
		this.transactionId = transactionId;
	}

	public EntityId getAccountId() {
		return accountId;
	}

	public EntityId getTransactionId() {
		return transactionId;
	}
}
