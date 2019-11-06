package uk.me.jasonmarston.legacy.repository.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import uk.me.jasonmarston.legacy.repository.BaseCrudRepository;
import uk.me.jasonmarston.legacy.util.BeanUtil;

public class BaseCrudRepositoryImpl<E, ID extends Serializable> extends SimpleJpaRepository<E, ID> implements BaseCrudRepository<E, ID> {
	private AutowireCapableBeanFactory beanFactory;

	public BaseCrudRepositoryImpl(Class<E> domainClass, EntityManager em) {
		super(domainClass, em);
	}

	public BaseCrudRepositoryImpl(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	@Transactional
	@Override
	public Optional<E> findById(final ID id) {
		final Optional<E> entity = super.findById(id);
		if(entity.isPresent()) {
			inject(entity.get());
		}
		return entity;
	}

	@Transactional
	@Override
	public List<E> findAll() {
		final Iterable<E> iterable = super.findAll();
		for(E entity: iterable) {
			inject(entity);
		}
		return super.findAll();
	}

	@Transactional
	@Override
	public List<E> findAllById(final Iterable<ID> ids) {
		final Iterable<E> iterable = super.findAll();
		for(E entity: iterable) {
			inject(entity);
		}
		return super.findAllById(ids);
	}

	private void inject(final E entity) {
		if(beanFactory == null) {
			beanFactory = BeanUtil.getAutowireCapableBeanFactory();
		}
		beanFactory.autowireBean(entity);
	}
}
