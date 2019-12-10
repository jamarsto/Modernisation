package uk.me.jasonmarston.framework.repository.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class AutoFlushJpaRepositoryFactoryBean<R extends JpaRepository<T, I>,
		T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

	private static class AutoFlushJpaRepositoryFactory<T, 
			I extends Serializable> extends JpaRepositoryFactory {
		private final EntityManager entityManager;

		public AutoFlushJpaRepositoryFactory(
				final EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@Override
		protected Class<?> getRepositoryBaseClass(
				final RepositoryMetadata metadata) {
			return AutoFlushJpaRepositoryImpl.class;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object getTargetRepository(
				final RepositoryInformation information) {
			return new AutoFlushJpaRepositoryImpl<T,I>(
					(Class<T>) information.getDomainType(),
					this.entityManager);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected SimpleJpaRepository<T, I> getTargetRepository(
				final RepositoryInformation information,
				final EntityManager entityManager) {
			return new AutoFlushJpaRepositoryImpl<T,I>(
					(Class<T>) information.getDomainType(),
					entityManager);
		}
	}

	public AutoFlushJpaRepositoryFactoryBean(
			final Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(
			final EntityManager entityManager) {
		return new AutoFlushJpaRepositoryFactory<T, I>(entityManager);
	}
}
