package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.status = 'AKTIVE'")
    Optional<User> findActiveUserByEmail(String email);

    Optional<User> findByEmail(String email);

    Page<User> findAll(Specification<User> userSpecification, Pageable pageable);

    Optional<User> findByCode(String code);
}
