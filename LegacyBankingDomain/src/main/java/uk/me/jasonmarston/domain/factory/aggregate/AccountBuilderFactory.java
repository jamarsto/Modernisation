package uk.me.jasonmarston.domain.factory.aggregate;

import uk.me.jasonmarston.domain.aggregate.Account;

public interface AccountBuilderFactory {
	Account.Builder create();
}
