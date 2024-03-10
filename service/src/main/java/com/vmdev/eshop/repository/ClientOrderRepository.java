package com.vmdev.eshop.repository;

import com.vmdev.eshop.entity.ClientOrder;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ClientOrderRepository extends RepositoryBase<Long, ClientOrder> {

    public ClientOrderRepository(EntityManager entityManager) {
        super(ClientOrder.class, entityManager);
    }
}
