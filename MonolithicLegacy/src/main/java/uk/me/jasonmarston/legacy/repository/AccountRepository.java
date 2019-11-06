package uk.me.jasonmarston.legacy.repository;

import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.legacy.domain.aggregate.impl.Account;
import uk.me.jasonmarston.legacy.domain.type.impl.EntityId;

@Repository
public interface AccountRepository extends BaseCrudRepository<Account, EntityId> {

}
