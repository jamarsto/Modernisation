package uk.me.jasonmarston.legacy;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import uk.me.jasonmarston.legacy.repository.impl.BaseCrudRepositoryFactoryBean;
import uk.me.jasonmarston.legacy.repository.impl.BaseCrudRepositoryImpl;

@Configuration
@EnableJpaRepositories(
		basePackages = {"uk.me.jasonmarston.legacy"},
		repositoryFactoryBeanClass = BaseCrudRepositoryFactoryBean.class, 
		repositoryBaseClass = BaseCrudRepositoryImpl.class)
@EnableTransactionManagement
public class PersistentContext {
}
