package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.constants.TestStatus;
import com.enigma.jobConnector.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TestRepository extends JpaRepository<Test, String>, JpaSpecificationExecutor<Test> {
    @Modifying
    @Query("UPDATE Test t SET t.status = :status WHERE t.deadlineAt < :now AND t.status = :currentStatus")
    void updateStatusToAwaiting(@Param("status") TestStatus status,
                                @Param("now") LocalDateTime now,
                                @Param("currentStatus") TestStatus currentStatus);
}
