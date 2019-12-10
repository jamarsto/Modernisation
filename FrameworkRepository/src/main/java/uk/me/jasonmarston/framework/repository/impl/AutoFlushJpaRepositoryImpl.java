package uk.me.jasonmarston.framework.repository.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

public class AutoFlushJpaRepositoryImpl<E, ID extends Serializable>
			extends SimpleJpaRepository<E, ID> {
	public AutoFlushJpaRepositoryImpl(final Class<E> domainClass,
			final EntityManager em) {
		super(domainClass, em);
	}

	public AutoFlushJpaRepositoryImpl(
			final JpaEntityInformation<E, ?> entityInformation,
			final EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	@Override
	public void flush() {
		try {
			super.flush();
		}
		catch(final DataIntegrityViolationException e) {
			throw new EntityExistsException(e.getMessage());
		}
		catch(final ObjectOptimisticLockingFailureException e) {
			throw new OptimisticLockException(e.getMessage());
		}
	}

	@Override
	public <S extends E> S save(final S entity) {
		try {
			final S localEntity = super.save(entity);
			this.flush();
			return localEntity;
		}
		catch(final DataIntegrityViolationException e) {
			throw new EntityExistsException(e.getMessage());
		}
		catch(final ObjectOptimisticLockingFailureException e) {
			throw new OptimisticLockException(e.getMessage());
		}
			
	}

	@Override
	public <S extends E> List<S> saveAll(final Iterable<S> entities) {
		try {
			final List<S> localEntities = super.saveAll(entities);
			this.flush();
			return localEntities;
		}
		catch(final DataIntegrityViolationException e) {
			throw new EntityExistsException(e.getMessage());
		}
		catch(final ObjectOptimisticLockingFailureException e) {
			throw new OptimisticLockException(e.getMessage());
		}
	}

	@Override
	public <S extends E> S saveAndFlush(final S entity) {
		try {
			return super.saveAndFlush(entity);
		}
		catch(final DataIntegrityViolationException e) {
			throw new EntityExistsException(e.getMessage());
		}
		catch(final ObjectOptimisticLockingFailureException e) {
			throw new OptimisticLockException(e.getMessage());
		}
	}
}
