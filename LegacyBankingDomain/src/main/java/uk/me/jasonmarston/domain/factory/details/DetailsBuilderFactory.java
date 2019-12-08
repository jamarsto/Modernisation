package uk.me.jasonmarston.domain.factory.details;

import uk.me.jasonmarston.domain.builder.IBuilder;
import uk.me.jasonmarston.framework.domain.DomainObject;

public interface DetailsBuilderFactory {
	<B extends IBuilder<? extends DomainObject>> B create(final Class<B> clazz);
}