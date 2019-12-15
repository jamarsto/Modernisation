package uk.me.jasonmarston.framework.domain.entity;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import uk.me.jasonmarston.framework.domain.AbstractDomainObject;
import uk.me.jasonmarston.framework.domain.IdentifiableDomainObject;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@MappedSuperclass
public abstract class AbstractEntity extends AbstractDomainObject 
		implements IdentifiableDomainObject {
	private static final long serialVersionUID = 1L;

	@Id
	@JsonUnwrapped
	@NotNull
	private EntityId id;

	protected AbstractEntity() {
		this.id = new EntityId();
	}

	protected ToStringBuilder addFieldsToToString() {
		return getToStringBuilder();
	}
	
	@Override
	public boolean equals(final Object obj) {
		return EqualsBuilder.reflectionEquals(
				this,
				obj,
				getExcludeFromUniqueness());
	}

	protected <T> T getBean(final Class<T> clazz) {
		return BeanHelper.INSTANCE.getBean(clazz);
	}

	protected String[] getExcludeFromUniqueness() {
		return new String[] {};
	}

	@Override
	public EntityId getId() {
		return id;
	}
	
	protected ReflectionToStringBuilder getToStringBuilder() {
		return new ReflectionToStringBuilder(
				this,
				ToStringStyle.MULTI_LINE_STYLE)
			.setExcludeFieldNames(getExcludeFromUniqueness());
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, getExcludeFromUniqueness());
	}
	
	@Override
	public String toString() {
		return getToStringBuilder().build();
	}
}