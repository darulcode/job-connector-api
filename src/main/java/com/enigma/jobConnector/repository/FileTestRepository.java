package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.entity.FileTest;
import com.enigma.jobConnector.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileTestRepository extends JpaRepository<FileTest, String> {
    Optional<FileTest> findByTest(Test test);
}
