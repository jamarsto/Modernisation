package uk.me.jasonmarston.legacy.domain.type.impl;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import uk.me.jasonmarston.legacy.domain.type.AbstractValueObject;

public class Transfer extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@JsonUnwrapped
	private Amount amount;

	@JsonUnwrapped
	private EntityId toAccount;
	
	public Transfer(final Amount amount, final EntityId toAccount) {
		this.amount = amount;
		this.toAccount = toAccount;
	}
}
