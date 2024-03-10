package com.vmdev.eshop.repository;

import com.vmdev.eshop.TestRepositoryBase;
import com.vmdev.eshop.entity.OrderProduct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProductRepositoryIT extends TestRepositoryBase {

    private static OrderProductRepository orderProductRepository;
    private final Long ORDER_PRODUCT_ID = 1L;

    @BeforeAll
    static void init() {
        orderProductRepository = context.getBean("orderProductRepository", OrderProductRepository.class);
    }

    @Test
    void findOrderProductById() {
        OrderProduct expectedResult = entityManager.find(OrderProduct.class, ORDER_PRODUCT_ID);
        Optional<OrderProduct> actualResult = orderProductRepository.findById(ORDER_PRODUCT_ID);

        assertThat(actualResult).isPresent();
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void findNothingIfOrderProductNotExist() {
        Optional<OrderProduct> actualResult = orderProductRepository.findById(100000L);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void findAllOrderProductsCheck() {
        List<OrderProduct> actualResult = orderProductRepository.findAll();

        assertThat(actualResult).hasSize(9);
    }

    @Test
    void createOrderProductCheck() {
        OrderProduct orderProduct = OrderProduct.builder()
                .quantity(10)
                .build();

        orderProductRepository.save(orderProduct);
        Optional<OrderProduct> actualResult = orderProductRepository.findById(orderProduct.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(orderProduct.getId());
    }

    @Test
    void deleteOrderProductCheck() {
        OrderProduct orderProduct = OrderProduct.builder()
                .quantity(10)
                .build();

        orderProductRepository.save(orderProduct);
        orderProductRepository.delete(orderProduct);
        Optional<OrderProduct> actualResult = orderProductRepository.findById(orderProduct.getId());

        assertThat(actualResult).isEmpty();
    }

    @Test
    void updateOrderProductCheck() {
        OrderProduct orderProduct = OrderProduct.builder()
                .quantity(10)
                .build();
        orderProduct.setQuantity(100);

        orderProductRepository.save(orderProduct);
        orderProductRepository.update(orderProduct);
        entityManager.flush();
        Optional<OrderProduct> actualResult = orderProductRepository.findById(orderProduct.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getQuantity()).isEqualTo(100);
    }

}