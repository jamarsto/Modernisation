package uk.me.jasonmarston.domain.factory;

import uk.me.jasonmarston.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.DomainObject;

public interface Factory<B extends IBuilder<? extends DomainObject>> {
	B create();
}