package uk.me.jasonmarston.domain.value;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@Embeddable
public class Balance extends AbstractValueObject {
	private static final long serialVersionUID = 1L;

	@NotNull
	@DecimalMin(value = "-9999999.99", inclusive = true)
	@DecimalMax(value = "9999999.99", inclusive = true)
	@Digits(integer = 7, fraction = 2)
	@Column(nullable = false, precision = 7, scale = 2)
	private BigDecimal balance;

	public Balance( ) {
	}

	public Balance(final String amountString) {
		this.balance = new BigDecimal(amountString);
	}

	public Balance add(final Amount amount) {
		this.balance = this.balance.add(amount.getAmount());
		return this;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public Balance subtract(final Amount amount) {
		this.balance = this.balance.subtract(amount.getAmount());
		return this;
	}
}