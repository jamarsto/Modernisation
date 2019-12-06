package uk.me.jasonmarston.domain.details;

import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.domain.value.Amount;
import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

public class TransferDetails extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull(message = "From Account Id is required")
	private EntityId fromAccountId;

	@NotNull(message = "To Account Id is required")
	private EntityId toAccountId;
	
	@NotNull(message = "Amount is required")
	private Amount amount;

	public TransferDetails(
			final EntityId fromAccountId,
			final EntityId toAccountId,
			final Amount amount) {
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
	}

	public Amount getAmount() {
		return amount;
	}

	public EntityId getFromAccountId() {
		return fromAccountId;
	}

	public EntityId getToAccountId() {
		return toAccountId;
	}
}
