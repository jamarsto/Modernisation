package uk.me.jasonmarston.legacy;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.me.jasonmarston.legacy.repository.impl.AutowireCrudRepositoryFactoryBean;
import uk.me.jasonmarston.legacy.repository.impl.AutowireJpaRepositoryImpl;

@Configuration
@EnableJpaRepositories(
		basePackages = {"uk.me.jasonmarston.legacy.repository.impl"},
		repositoryFactoryBeanClass = AutowireCrudRepositoryFactoryBean.class,
		repositoryBaseClass = AutowireJpaRepositoryImpl.class)
@EnableTransactionManagement
public class PersistentContext {
}
