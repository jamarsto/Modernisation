package uk.me.jasonmarston.domain.factory.details.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import uk.me.jasonmarston.domain.builder.IBuilder;
import uk.me.jasonmarston.domain.details.RegistrationDetails;
import uk.me.jasonmarston.domain.details.TransactionDetails;
import uk.me.jasonmarston.domain.details.TransactionIdentifierDetails;
import uk.me.jasonmarston.domain.details.TransferDetails;
import uk.me.jasonmarston.domain.factory.Factory;
import uk.me.jasonmarston.domain.factory.details.DetailsBuilderFactory;
import uk.me.jasonmarston.domain.factory.details.RegistrationDetailsBuilderFactory;
import uk.me.jasonmarston.domain.factory.details.TransactionDetailsBuilderFactory;
import uk.me.jasonmarston.domain.factory.details.TransactionIdentifierDetailsBuilderFactory;
import uk.me.jasonmarston.domain.factory.details.TransferDetailsBuilderFactory;
import uk.me.jasonmarston.framework.domain.DomainObject;

@Service
public class DetailsBuilderFactoryImpl implements DetailsBuilderFactory {
	@Autowired
	@Lazy
	private RegistrationDetailsBuilderFactory registrationDetailsBuilderFactory;

	@Autowired
	@Lazy
	private TransactionDetailsBuilderFactory transactionDetailsBuilderFactory;

	@Autowired
	@Lazy
	private TransactionIdentifierDetailsBuilderFactory transactionIdentifierDetailsBuilderFactory;

	@Autowired
	@Lazy
	private TransferDetailsBuilderFactory transferDetailsBuilderFactory;
	
	private volatile Map<Class<? extends IBuilder<?>>, Factory<? extends IBuilder<? extends DomainObject>>> map;
	
	private Object lock = new Object();

	public DetailsBuilderFactoryImpl() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public <B extends IBuilder<? extends DomainObject>> B create(Class<B> clazz) {
		if(getMap().containsKey(clazz)) {
			return (B) getMap().get(clazz).create();
		}
		throw new RuntimeException(clazz.getName() + " not registered with DetailsBuilderFactory");
	}

	private Map<Class<? extends IBuilder<?>>, Factory<? extends IBuilder<? extends DomainObject>>> getMap() {
		if(map == null) {
			synchronized(lock) {
				if(map == null) {
					map = new HashMap<Class<? extends IBuilder<?>>, Factory<? extends IBuilder<? extends DomainObject>>>();
					map.put(RegistrationDetails.Builder.class, registrationDetailsBuilderFactory);
					map.put(TransactionDetails.Builder.class, transactionDetailsBuilderFactory);
					map.put(TransactionIdentifierDetails.Builder.class, transactionIdentifierDetailsBuilderFactory);
					map.put(TransferDetails.Builder.class, transferDetailsBuilderFactory);
				}
			}
		}
		return map;
	}
}