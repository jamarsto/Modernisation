package uk.me.jasonmarston;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.me.jasonmarston.framework.repository.impl.AutoFlushJpaRepositoryFactoryBean;
import uk.me.jasonmarston.framework.repository.impl.AutoFlushJpaRepositoryImpl;
@Configuration
@EnableJpaRepositories(basePackages = {"uk.me.jasonmarston.domain.repository"},
		repositoryFactoryBeanClass = AutoFlushJpaRepositoryFactoryBean.class,
		repositoryBaseClass = AutoFlushJpaRepositoryImpl.class)
@EnableTransactionManagement
public class PersistenceConfig {
}