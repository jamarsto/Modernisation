package uk.me.jasonmarston.domain.factory.aggregate;

import uk.me.jasonmarston.domain.aggregate.User;

public interface UserBuilderFactory {
	User.Builder create();
}
