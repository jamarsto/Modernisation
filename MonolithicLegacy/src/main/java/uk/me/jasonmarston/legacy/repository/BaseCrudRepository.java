package uk.me.jasonmarston.legacy.repository;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseCrudRepository<E, ID extends Serializable> extends CrudRepository<E, ID> {
}
