package uk.me.jasonmarston.legacy.repository.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import uk.me.jasonmarston.legacy.util.BeanUtil;

public class AutowireJpaRepositoryImpl<E, ID extends Serializable>
			extends SimpleJpaRepository<E, ID> {
	private AutowireCapableBeanFactory beanFactory;

	public AutowireJpaRepositoryImpl(final Class<E> domainClass,
			final EntityManager em) {
		super(domainClass, em);
	}

	public AutowireJpaRepositoryImpl(
			final JpaEntityInformation<E, ?> entityInformation,
			final EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	@Override
	public List<E> findAll() {
		final List<E> iterable = super.findAll();
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public <S extends E> List<S> findAll(final Example<S> example) {
		final List<S> iterable = super.findAll(example);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public <S extends E> Page<S> findAll(final Example<S> example,
			final Pageable pageable) {
		final Page<S> iterable = super.findAll(example, pageable);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public <S extends E> List<S> findAll(final Example<S> example,
			final Sort sort) {
		final List<S> iterable = super.findAll(example, sort);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public Page<E> findAll(final Pageable pageable) {
		final Page<E> iterable = super.findAll(pageable);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public List<E> findAll(final Sort sort) {
		final List<E> iterable = super.findAll(sort);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public List<E> findAll(final Specification<E> spec) {
		final List<E> iterable = super.findAll(spec);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public Page<E> findAll(final Specification<E> spec,
			final Pageable pageable) {
		final Page<E> iterable = super.findAll(spec, pageable);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public List<E> findAll(final Specification<E> spec, final Sort sort) {
		final List<E> iterable = super.findAll(spec, sort);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public List<E> findAllById(final Iterable<ID> ids) {
		final List<E> iterable = super.findAllById(ids);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public Optional<E> findById(final ID id) {
		final Optional<E> entity = super.findById(id);
		if(entity.isPresent()) {
			inject(entity.get());
		}
		return entity;
	}

	@Override
	public <S extends E> Optional<S> findOne(final Example<S> example) {
		final Optional<S> entity = super.findOne(example);
		if(entity.isPresent()) {
			inject(entity.get());
		}
		return entity;
	}

	@Override
	public Optional<E> findOne(final Specification<E> spec) {
		final Optional<E> entity = super.findOne(spec);
		if(entity.isPresent()) {
			inject(entity.get());
		}
		return entity;
	}

	@Override
	public E getOne(final ID id) {
		final E entity = super.getOne(id);
		if(entity != null) {
			inject(entity);
		}
		return entity;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Page<E> readPage(final TypedQuery<E> query,
			final Pageable pageable,
			final Specification<E> spec) {
		final Page<E> iterable = super.readPage(query, pageable, spec);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	protected <S extends E> Page<S> readPage(final TypedQuery<S> query,
			final Class<S> domainClass,
			final Pageable pageable,
			final Specification<S> spec) {
		final Page<S> iterable = super
				.readPage(query, domainClass, pageable, spec);
		for(E entity: iterable) {
			inject(entity);
		}
		return iterable;
	}

	@Override
	public <S extends E> S save(final S entity) {
		final S localEntity = super.save(entity);
		if(localEntity != null) {
			inject(localEntity);
		}
		return localEntity;
	}

	@Override
	public <S extends E> List<S> saveAll(final Iterable<S> entities) {
		final List<S> localEntities = super.saveAll(entities);
		for(S entity: localEntities) {
			inject(entity);
		}
		return localEntities;
	}

	@Override
	public <S extends E> S saveAndFlush(final S entity) {
		final S localEntity = super.saveAndFlush(entity);
		if(localEntity != null) {
			inject(localEntity);
		}
		return localEntity;
	}

	private void inject(final E entity) {
		if(beanFactory == null) {
			beanFactory = BeanUtil.getAutowireCapableBeanFactory();
		}
		beanFactory.autowireBean(entity);
	}
}
