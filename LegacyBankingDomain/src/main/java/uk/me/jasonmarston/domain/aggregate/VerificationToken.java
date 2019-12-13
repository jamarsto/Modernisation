package uk.me.jasonmarston.domain.aggregate;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.factory.aggregate.VerificationTokenBuilderFactory;
import uk.me.jasonmarston.domain.value.Token;
import uk.me.jasonmarston.framework.domain.aggregate.AbstractAggregate;
import uk.me.jasonmarston.framework.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Entity
@Table(name = "VERIFICATION_TOKENS")
public class VerificationToken extends AbstractAggregate {
	public static class Builder implements IBuilder<VerificationToken> {
		private EntityId userId;

		private Builder() {
		}

		@Override
		public VerificationToken build() {
			if(userId == null) {
				throw new RuntimeException("A User ID is required");
			}

			final VerificationToken verificationToken = new VerificationToken();
			verificationToken.userId = userId;

			return verificationToken;
		}

		public Builder forUserId(EntityId userId) {
			this.userId = userId;
			return this;
		}
	}

	@Service
	public static class Factory implements VerificationTokenBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@NotNull
	private Token token;

	@AttributeOverride(name="id", column=@Column(name="userId"))
	@NotNull
	private EntityId userId;
	
	@NotNull
	private Date expiryDate;

	private VerificationToken() {
		super();
		this.token = new Token(UUID.randomUUID().toString());
		this.expiryDate = calculateExpiryDate();
	}

	private Date calculateExpiryDate() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, 60);
		return new Date(cal.getTime().getTime());
    }

	public Date getExpiryDate() {
		return expiryDate;
	}

	public Token getToken() {
		return token;
	}

	public EntityId getUserId() {
		return userId;
	}

	public boolean isExpired() {
		final Calendar cal = Calendar.getInstance();
		final long now = cal.getTime().getTime();
		final long timeRemaining = expiryDate.getTime() - now;
		return timeRemaining <= 0;
	}
}