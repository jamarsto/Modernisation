package uk.me.jasonmarston.legacy.domain.aggregate.factory.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;

import uk.me.jasonmarston.legacy.domain.aggregate.factory.AccountFactory;
import uk.me.jasonmarston.legacy.domain.aggregate.impl.Account;

@Service
public class AccountFactoryImpl implements AccountFactory {
	
	@Autowired
	private AutowireCapableBeanFactory beanFactory;

	@Override
	public Account create() {
		Account account = new Account();
		beanFactory.autowireBean(account);
		return account;
	}
}
