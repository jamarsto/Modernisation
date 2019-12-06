package uk.me.jasonmarston.framework.domain.type.impl;

import java.util.UUID;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import uk.me.jasonmarston.framework.domain.type.AbstractValueObject;

@Embeddable
public class EntityId extends AbstractValueObject {
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "Id is required")
	private UUID id;
	
	public EntityId() {
		this.id = UUID.randomUUID();
	}

	public EntityId(final String stringId) {
		this.id = UUID.fromString(stringId);
	}
	
	public EntityId(final UUID id) {
		this.id = id;
	}

	public UUID getId() {
		return id;
	}
}