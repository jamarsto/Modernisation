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

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.factory.aggregate.VerificationTokenBuilderFactory;
import uk.me.jasonmarston.framework.domain.aggregate.AbstractAggregate;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Configurable(preConstruction = true, autowire = Autowire.BY_TYPE, dependencyCheck = false)
@Entity
@Table(name = "VERIFICATION_TOKEN")
public class VerificationToken extends AbstractAggregate {
	public static class Builder {
		private EntityId userId;

		private Builder() {
		}
		
		public VerificationToken build() {
			if(userId == null) {
				throw new RuntimeException("A User ID is required");
			}
			final VerificationToken verificationToken = new VerificationToken(userId);
			return verificationToken;
		}

		public Builder forUserId(EntityId userId) {
			this.userId = userId;
			return this;
		}
	}
	@Service
	public static class FactoryImpl implements VerificationTokenBuilderFactory {
		@Override
		public Builder create() {
			return new Builder();
		}
	}

	private static final long serialVersionUID = 1L;

	@NotNull
	private String token;

	@AttributeOverride(name="id", column=@Column(name="userId"))
	@NotNull
	private EntityId userId;
	
	@NotNull
	private Date expiryDate;
	
	private VerificationToken() {
	}
	
	private VerificationToken(final EntityId userId) {
		this.setId(new EntityId());
		this.userId = userId;
		this.token = UUID.randomUUID().toString();
		this.expiryDate = calculateExpiryDate();
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public void setUserId(EntityId userId) {
		this.userId = userId;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	private Date calculateExpiryDate() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Timestamp(cal.getTime().getTime()));
		cal.add(Calendar.MINUTE, 60);
		return new Date(cal.getTime().getTime());
    }
}