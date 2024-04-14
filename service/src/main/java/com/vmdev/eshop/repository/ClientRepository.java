package com.vmdev.eshop.repository;

import com.vmdev.eshop.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long>,
        QuerydslPredicateExecutor<Client> {

    Optional<Client> findByEmail(String username);

}
