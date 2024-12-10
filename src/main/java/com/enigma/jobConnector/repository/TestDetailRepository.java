package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.entity.Test;
import com.enigma.jobConnector.entity.TestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestDetailRepository extends JpaRepository<TestDetail, String> {
    List<TestDetail> findAllByUserId(String id);

    List<TestDetail> findAllByTestId(String testId);

    Optional<TestDetail> findByTestAndUserId(Test test, String userId);
}
