package com.enigma.jobConnector.repository;

import com.enigma.jobConnector.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String>, JpaSpecificationExecutor<Client> {


    Optional<Client> findByName(String name);
}
