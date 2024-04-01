package com.vmdev.eshop.repository;

import com.vmdev.eshop.entity.ClientOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {

    @EntityGraph(attributePaths = {"products", "products.product", "client"})
    List<ClientOrder> findAllByClientId(Long clientId);
}
