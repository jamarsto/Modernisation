package uk.me.jasonmarston.legacy.domain.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import uk.me.jasonmarston.legacy.domain.AbstractDomainObject;
import uk.me.jasonmarston.legacy.domain.IdentifiableDomainObject;
import uk.me.jasonmarston.legacy.domain.type.impl.EntityId;

@MappedSuperclass
public abstract class AbstractEntity extends AbstractDomainObject 
		implements IdentifiableDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@JsonUnwrapped
	private EntityId id;

	@Override
	public EntityId getId() {
		return id;
	}
	
	protected void setId(EntityId id) {
		this.id = id;
	}
}
