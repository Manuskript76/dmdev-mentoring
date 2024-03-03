package com.vmdev.eshop.dao;

import com.vmdev.eshop.entity.OrderProduct;
import jakarta.persistence.EntityManager;

public class OrderProductRepository extends RepositoryBase<Long, OrderProduct> {
    public OrderProductRepository(EntityManager entityManager) {
        super(OrderProduct.class, entityManager);
    }
}
