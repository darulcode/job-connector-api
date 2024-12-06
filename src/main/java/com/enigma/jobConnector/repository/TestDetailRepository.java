package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.entity.TestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestDetailRepository extends JpaRepository<TestDetail, String> {
}
