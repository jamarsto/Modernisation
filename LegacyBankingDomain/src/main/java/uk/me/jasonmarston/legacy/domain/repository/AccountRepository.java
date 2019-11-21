package uk.me.jasonmarston.legacy.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.framework.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.domain.aggregate.impl.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, EntityId> {

}
