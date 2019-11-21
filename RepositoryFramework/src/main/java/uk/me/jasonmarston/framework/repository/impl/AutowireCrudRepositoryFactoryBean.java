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

public class AutowireCrudRepositoryFactoryBean<R extends JpaRepository<T, I>,
		T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

	public AutowireCrudRepositoryFactoryBean(
			final Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(
			final EntityManager entityManager) {
		return new AutowireCrudRepositoryFactory<T, I>(entityManager);
	}
	
	private static class AutowireCrudRepositoryFactory<T, 
			I extends Serializable> extends JpaRepositoryFactory {
		private final EntityManager entityManager;

		public AutowireCrudRepositoryFactory(
				final EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object getTargetRepository(
				final RepositoryInformation information) {
			return new AutowireJpaRepositoryImpl<T,I>(
					(Class<T>) information.getDomainType(),
					this.entityManager);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected SimpleJpaRepository<T, I> getTargetRepository(
				final RepositoryInformation information,
				final EntityManager entityManager) {
			return new AutowireJpaRepositoryImpl<T,I>(
					(Class<T>) information.getDomainType(),
					entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(
				final RepositoryMetadata metadata) {
			return AutowireJpaRepositoryImpl.class;
		}
	}
}
