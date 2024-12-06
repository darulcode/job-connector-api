package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.entity.FileTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTestRepository extends JpaRepository<FileTest, String> {
}
