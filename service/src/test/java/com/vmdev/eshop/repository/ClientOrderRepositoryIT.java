package com.vmdev.eshop.repository;

import com.vmdev.eshop.entity.ClientOrder;
import com.vmdev.eshop.entity.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
class ClientOrderRepositoryIT extends IntegrationTestBase {

    private final ClientOrderRepository clientOrderRepository;

    @Test
    void findClientOrderById() {
        ClientOrder clientOrder = ClientOrder.builder()
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .build();

        entityManager.persist(clientOrder);
        ClientOrder expectedResult = entityManager.find(ClientOrder.class, clientOrder.getId());
        Optional<ClientOrder> actualResult = clientOrderRepository.findById(clientOrder.getId());

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
        entityManager.flush();
        Optional<ClientOrder> actualResult = clientOrderRepository.findById(clientOrder.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getStatus()).isSameAs(OrderStatus.COMPLETED);
    }

}