package uk.me.jasonmarston;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.me.jasonmarston.framework.repository.impl.AutowireCrudRepositoryFactoryBean;
import uk.me.jasonmarston.framework.repository.impl.AutowireJpaRepositoryImpl;

@Configuration
@EnableJpaRepositories(
		basePackages = {"uk.me.jasonmarston.domain.repository"},
		repositoryFactoryBeanClass = AutowireCrudRepositoryFactoryBean.class,
		repositoryBaseClass = AutowireJpaRepositoryImpl.class)
@EnableTransactionManagement
public class PersistentContext {
}