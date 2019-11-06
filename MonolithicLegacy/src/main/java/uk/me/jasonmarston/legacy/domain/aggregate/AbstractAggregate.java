package uk.me.jasonmarston.legacy.domain.aggregate;

import javax.persistence.MappedSuperclass;

import uk.me.jasonmarston.legacy.domain.entity.AbstractEntity;

@MappedSuperclass
public class AbstractAggregate extends AbstractEntity {
	private static final long serialVersionUID = 1L;
}
