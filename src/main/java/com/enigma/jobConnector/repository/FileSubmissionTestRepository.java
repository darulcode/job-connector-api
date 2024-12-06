package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.entity.FileSubmissionTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileSubmissionTestRepository extends JpaRepository<FileSubmissionTest, String> {
}
