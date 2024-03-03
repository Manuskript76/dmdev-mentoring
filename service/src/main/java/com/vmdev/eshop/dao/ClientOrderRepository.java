package com.vmdev.eshop.dao;

import com.vmdev.eshop.entity.ClientOrder;
import jakarta.persistence.EntityManager;

public class ClientOrderRepository extends RepositoryBase<Long, ClientOrder> {
    public ClientOrderRepository(EntityManager entityManager) {
        super(ClientOrder.class, entityManager);
    }
}
