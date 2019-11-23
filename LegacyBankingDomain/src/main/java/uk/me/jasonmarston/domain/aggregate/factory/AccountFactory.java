package uk.me.jasonmarston.domain.aggregate.factory;

import uk.me.jasonmarston.domain.aggregate.impl.Account;

public interface AccountFactory {
	Account create();
}
