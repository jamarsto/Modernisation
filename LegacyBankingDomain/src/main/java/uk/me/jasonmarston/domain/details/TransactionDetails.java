package uk.me.jasonmarston.domain.details;

import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.value.Amount;
import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class TransactionDetails extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "Account Id is required")
	private EntityId accountId;
	
	@NotNull(message = "Amount is required")
	private Amount amount;
	
	public TransactionDetails() {
	}

	public TransactionDetails(final EntityId accountId, final Amount amount) {
		this.accountId = accountId;
		this.amount = amount;
	}

	public EntityId getAccountId() {
		return accountId;
	}

	public Amount getAmount() {
		return amount;
	}
}
