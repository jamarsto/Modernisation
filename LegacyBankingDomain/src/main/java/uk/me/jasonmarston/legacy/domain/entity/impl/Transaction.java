package uk.me.jasonmarston.legacy.domain.entity.impl;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import uk.me.jasonmarston.framework.domain.entity.AbstractEntity;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.domain.aggregate.impl.Account;
import uk.me.jasonmarston.legacy.domain.type.impl.Amount;
import uk.me.jasonmarston.legacy.domain.type.impl.TransactionType;

@Entity(name = "TRANSACTIONS")
public class Transaction extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.ORDINAL)
	private TransactionType type;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	@AttributeOverride(name="id", column=@Column(name="ownerAccountId"))
	private Account account;

	@JsonInclude
	@JsonUnwrapped
	@Transient
	private EntityId ownerAccountId;

	@JsonUnwrapped
	private Amount amount;

	@JsonUnwrapped
	@AttributeOverride(name="id", column=@Column(name="referenceAccountId"))
	private EntityId referenceAccountId;
	
	public Transaction() {
		this.setId(new EntityId());
	}
	
	public Transaction(TransactionType type, 
			Account account, 
			Amount amount, 
			EntityId referenceAccountId) {
		this();
		this.type = type;
		this.account = account;
		this.ownerAccountId = account.getId();
		this.amount = amount;
		this.referenceAccountId = referenceAccountId;
	}

	public TransactionType getType() {
		return type;
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
}
