package uk.me.jasonmarston.domain.aggregate.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import uk.me.jasonmarston.framework.domain.aggregate.AbstractAggregate;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Entity
@Table(name = "VERIFICATION_TOKEN")
public class VerificationToken extends AbstractAggregate {
	private static final long serialVersionUID = 1L;

	private String token;

	@AttributeOverride(name="id", column=@Column(name="userId"))
	private EntityId userId;
	private Date expiryDate;
	
	public VerificationToken() {
		this.setId(new EntityId());
	}
	
	public VerificationToken(final EntityId userId) {
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