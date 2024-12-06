package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.entity.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCategoryRepository  extends JpaRepository<UserCategory,String>, JpaSpecificationExecutor<UserCategory> {
    Optional<UserCategory> findByName(String name);
}
