package com.vmdev.eshop.repository;

import com.vmdev.eshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, FilterProductRepository {

    @Query("select p from Product p " +
            "join p.orderProducts op " +
            "where op.id in :clientOrderIds")
    List<Product> findAllByClientOrderId(List<Long> clientOrderIds);
}
