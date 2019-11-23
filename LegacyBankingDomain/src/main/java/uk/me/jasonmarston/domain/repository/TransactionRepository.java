package uk.me.jasonmarston.domain.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.domain.entity.impl.Transaction;
import uk.me.jasonmarston.framework.domain.type.impl.EntityId;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, EntityId>, JpaSpecificationExecutor<Transaction> {
}
