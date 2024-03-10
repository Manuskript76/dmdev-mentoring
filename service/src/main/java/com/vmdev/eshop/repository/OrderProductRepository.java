package com.vmdev.eshop.repository;

import com.vmdev.eshop.entity.OrderProduct;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class OrderProductRepository extends RepositoryBase<Long, OrderProduct> {
    public OrderProductRepository(EntityManager entityManager) {
        super(OrderProduct.class, entityManager);
    }
}
