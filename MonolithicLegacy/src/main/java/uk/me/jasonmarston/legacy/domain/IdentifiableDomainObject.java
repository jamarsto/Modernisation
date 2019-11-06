package uk.me.jasonmarston.legacy.domain;

import uk.me.jasonmarston.legacy.domain.type.impl.EntityId;

public interface IdentifiableDomainObject extends DomainObject {
	EntityId getId();
}
