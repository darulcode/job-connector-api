package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.entity.FileSubmissionTest;
import com.enigma.jobConnector.entity.TestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileSubmissionTestRepository extends JpaRepository<FileSubmissionTest, String> {
    Optional<FileSubmissionTest> findByTestDetail(TestDetail testDetail);
}
