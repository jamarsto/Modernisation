package uk.me.jasonmarston.legacy.domain.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.framework.domain.type.impl.EntityId;
import uk.me.jasonmarston.legacy.domain.entity.impl.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, EntityId>, JpaSpecificationExecutor<Transaction> {
}
