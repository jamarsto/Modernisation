package uk.me.jasonmarston.legacy.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uk.me.jasonmarston.legacy.domain.entity.impl.Transaction;
import uk.me.jasonmarston.legacy.domain.type.impl.EntityId;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, EntityId>, JpaSpecificationExecutor<Transaction> {
}
