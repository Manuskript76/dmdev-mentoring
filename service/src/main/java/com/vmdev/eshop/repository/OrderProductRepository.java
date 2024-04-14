package com.vmdev.eshop.repository;

import com.vmdev.eshop.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    Optional<OrderProduct> findByClientOrderIdAndProductId(Long orderId, Long productId);
}
