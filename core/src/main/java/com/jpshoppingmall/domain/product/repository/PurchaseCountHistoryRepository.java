package com.jpshoppingmall.domain.product.repository;

import com.jpshoppingmall.domain.product.entity.PurchaseCountHistory;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseCountHistoryRepository extends JpaRepository<PurchaseCountHistory, Long>,
    PurchaseCountHistoryCustomRepository {

    @Modifying
    @Query("DELETE "
        + "FROM PurchaseCountHistory p "
        + "WHERE p.createdDateTime < :standardTime")
    void deleteAllByBeforeStandardTime(@Param("standardTime") LocalDateTime standardTime);
}
