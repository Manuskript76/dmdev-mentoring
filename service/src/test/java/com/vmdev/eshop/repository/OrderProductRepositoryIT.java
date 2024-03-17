package com.vmdev.eshop.repository;

import com.vmdev.eshop.entity.OrderProduct;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
class OrderProductRepositoryIT extends IntegrationTestBase {

    private final OrderProductRepository orderProductRepository;

    @Test
    void findOrderProductById() {
        OrderProduct orderProduct = OrderProduct.builder()
                .quantity(10)
                .build();

        entityManager.persist(orderProduct);
        OrderProduct expectedResult = entityManager.find(OrderProduct.class, orderProduct.getId());
        Optional<OrderProduct> actualResult = orderProductRepository.findById(orderProduct.getId());

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