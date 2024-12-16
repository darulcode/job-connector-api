package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.constants.TestStatus;
import com.enigma.jobConnector.entity.Test;
import com.enigma.jobConnector.entity.TestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TestDetailRepository extends JpaRepository<TestDetail, String>, JpaSpecificationExecutor<TestDetail> {

    List<TestDetail> findAllByTestId(String testId);

    Optional<TestDetail> findByTestAndUserId(Test test, String userId);


    @Modifying
    @Query("UPDATE Test t SET t.status = :status " +
            "WHERE t IN (SELECT td.test FROM TestDetail td WHERE td.test.deadlineAt < :now AND t.status = :currentStatus)")
    void updateTestStatusToAwaiting(@Param("status") TestStatus status,
                                    @Param("now") LocalDateTime now,
                                    @Param("currentStatus") TestStatus currentStatus);
}
