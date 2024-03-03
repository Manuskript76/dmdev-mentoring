package com.vmdev.eshop.dao;

import com.vmdev.eshop.entity.ClientOrder;
import com.vmdev.eshop.entity.enums.OrderStatus;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientOrderRepositoryIT {

    private static SessionFactory sessionFactory;
    private Session session;
    private ClientOrderRepository clientOrderRepository;

    @BeforeAll
    static void buildSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.importData(sessionFactory);
    }

    @BeforeEach
    void init() {
        session = SessionProxy.getSession(sessionFactory);
        clientOrderRepository = new ClientOrderRepository(session);
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
    void findClientOrderById() {
        ClientOrder expectedResult = session.get(ClientOrder.class, 1L);
        Optional<ClientOrder> actualResult = clientOrderRepository.findById(1L);

        assertThat(actualResult).isPresent();
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void findNothingIfClientOrderNotExist() {
        Optional<ClientOrder> actualResult = clientOrderRepository.findById(100000L);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void findAllClientOrdersCheck() {
        List<ClientOrder> actualResult = clientOrderRepository.findAll();

        assertThat(actualResult).hasSize(5);
    }

    @Test
    void createClientOrderCheck() {
        ClientOrder clientOrder = ClientOrder.builder()
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .build();

        clientOrderRepository.save(clientOrder);
        Optional<ClientOrder> actualResult = clientOrderRepository.findById(clientOrder.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(clientOrder.getId());
    }

    @Test
    void deleteClientOrderCheck() {
        ClientOrder clientOrder = ClientOrder.builder()
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .build();

        clientOrderRepository.save(clientOrder);
        clientOrderRepository.delete(clientOrder);
        Optional<ClientOrder> actualResult = clientOrderRepository.findById(clientOrder.getId());

        assertThat(actualResult).isEmpty();
    }

    @Test
    void updateClientOrderCheck() {
        ClientOrder clientOrder = ClientOrder.builder()
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .build();
        clientOrder.setStatus(OrderStatus.COMPLETED);

        clientOrderRepository.save(clientOrder);
        clientOrderRepository.update(clientOrder);
        session.flush();
        Optional<ClientOrder> actualResult = clientOrderRepository.findById(clientOrder.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getStatus()).isSameAs(OrderStatus.COMPLETED);
    }

}