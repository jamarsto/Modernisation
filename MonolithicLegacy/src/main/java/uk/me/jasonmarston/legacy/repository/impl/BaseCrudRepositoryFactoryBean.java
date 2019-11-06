package uk.me.jasonmarston.legacy.repository.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

public class BaseCrudRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable> extends JpaRepositoryFactoryBean<R, T, I> {

	public BaseCrudRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new BaseCrudRepositoryFactory(entityManager);
	}
	
	private static class BaseCrudRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
		private final EntityManager entityManager;

		public BaseCrudRepositoryFactory(EntityManager entityManager) {
			super(entityManager);
			this.entityManager = entityManager;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected Object getTargetRepository(RepositoryInformation information) {
			return new BaseCrudRepositoryImpl<T,I>((Class<T>) information.getDomainType(), this.entityManager);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected SimpleJpaRepository<?, ?> getTargetRepository(RepositoryInformation information,
				EntityManager entityManager) {
			return new BaseCrudRepositoryImpl<T,I>((Class<T>) information.getDomainType(), entityManager);
		}

		@Override
		protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
			return BaseCrudRepositoryImpl.class;
		}
	}
}
