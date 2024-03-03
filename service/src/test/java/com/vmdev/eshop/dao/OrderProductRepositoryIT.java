package com.vmdev.eshop.dao;

import com.vmdev.eshop.entity.OrderProduct;
import com.vmdev.eshop.util.HibernateTestUtil;
import com.vmdev.eshop.util.SessionProxy;
import com.vmdev.eshop.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProductRepositoryIT {

    private static SessionFactory sessionFactory;
    private Session session;
    private OrderProductRepository orderProductRepository;

    @BeforeAll
    static void buildSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.importData(sessionFactory);
    }

    @BeforeEach
    void init() {
        session = SessionProxy.getSession(sessionFactory);
        orderProductRepository = new OrderProductRepository(session);
        session.beginTransaction();
    }

    @AfterEach
    void close() {
        session.getTransaction().rollback();
        session.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @Test
    void findOrderProductById() {
        OrderProduct expectedResult = session.get(OrderProduct.class, 1L);
        Optional<OrderProduct> actualResult = orderProductRepository.findById(1L);

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
        session.flush();
        Optional<OrderProduct> actualResult = orderProductRepository.findById(orderProduct.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getQuantity()).isEqualTo(100);
    }

}