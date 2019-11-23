package uk.me.jasonmarston.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.domain.aggregate.impl.Account;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
public interface AccountRepository extends CrudRepository<Account, EntityId> {

}
