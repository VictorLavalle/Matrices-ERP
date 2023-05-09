package com.erpMatrices.mvp.repositories;

import com.erpMatrices.mvp.entities.Matrix;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

/**
 * This interface provides an abstraction for interacting with the table of matrices in the database.
 * It extends the JpaRepository interface to inherit CRUD operations and adds custom methods.
 * The @Lock annotation is used to configure a pessimistic lock for the method 'findWithLockById'.
 * This lock mode ensures that no other transaction can acquire a lock on the row with the same ID until
 * the current transaction is completed.
 * The @QueryHints annotation is used to provide query-level hints to the underlying JPA provider.
 * In this case, it specifies a timeout of 5 seconds for acquiring the lock. If the lock cannot be
 * acquired within this timeout, a javax.persistence.PessimisticLockException is thrown.
 */
@Repository
public interface MatrixRepository extends JpaRepository<Matrix, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({ @QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000") })
    Optional<Matrix> findWithLockById(Long matrixId);
}
