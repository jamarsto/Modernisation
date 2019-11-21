package uk.me.jasonmarston.framework.domain.aggregate;

import javax.persistence.MappedSuperclass;

import uk.me.jasonmarston.framework.domain.entity.AbstractEntity;

@MappedSuperclass
public class AbstractAggregate extends AbstractEntity {
	private static final long serialVersionUID = 1L;
}
