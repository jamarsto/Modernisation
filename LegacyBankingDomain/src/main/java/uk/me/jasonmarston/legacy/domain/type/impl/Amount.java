package uk.me.jasonmarston.legacy.domain.type.impl;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@Embeddable
public class Amount extends AbstractValueObject {
	private static final long serialVersionUID = 1L;
	private BigDecimal amount;

	public Amount( ) {
		this.amount = new BigDecimal("0.00");
	}

	public Amount(final String amountString) {
		this.amount = new BigDecimal(amountString);
	}

	public Amount(final BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Amount add(final Amount amount) {
		this.amount = this.amount.add(amount.getAmount());
		return this;
	}

	public Amount subtract(final Amount amount) {
		this.amount = this.amount.subtract(amount.getAmount());
		return this;
	}
}
