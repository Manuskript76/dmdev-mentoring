package com.vmdev.eshop.service;

import com.vmdev.eshop.dto.ClientCreateEditDto;
import com.vmdev.eshop.dto.ClientOrderDto;
import com.vmdev.eshop.dto.ClientReadDto;
import com.vmdev.eshop.entity.enums.OrderStatus;
import com.vmdev.eshop.entity.enums.Role;
import com.vmdev.eshop.repository.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ClientOrderServiceIT extends IntegrationTestBase {

    private final ClientOrderService clientOrderService;
    private final ClientService clientService;

    @Test
    void findAllByClient() {
        ClientReadDto user = getUser();
        ClientOrderDto expectedOrder1 = clientOrderService.create(user.getId());
        ClientOrderDto expectedOrder2 = clientOrderService.create(user.getId());

        List<ClientOrderDto> actualResult = clientOrderService.findAllByClient(user.getId());

        assertThat(actualResult).hasSize(2);
        assertThat(actualResult.get(0)).isEqualTo(expectedOrder1);
        assertThat(actualResult.get(1)).isEqualTo(expectedOrder2);
    }

    @Test
    void findById() {
        ClientReadDto user = getUser();
        ClientOrderDto expectedResult = clientOrderService.create(user.getId());

        Optional<ClientOrderDto> actualResult = clientOrderService.findById(expectedResult.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(expectedResult);

    }

    @Test
    void findByClientUsername() {
        ClientReadDto user = getUser();
        ClientOrderDto expectedOrder = clientOrderService.create(user.getId());

        Optional<ClientOrderDto> actualResult = clientOrderService.findByClientUsername(user.getEmail());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(expectedOrder);
    }

    @Test
    void create() {
        ClientReadDto user = getUser();

        ClientOrderDto order = clientOrderService.create(user.getId());
        Optional<ClientOrderDto> actualResult = clientOrderService.findById(order.getId());

        assertThat(actualResult).isPresent();
    }

    @Test
    void update() {
        ClientReadDto user = getUser();

        ClientOrderDto order = clientOrderService.create(user.getId());
        ClientOrderDto updateOrder = ClientOrderDto.builder()
                .id(order.getId())
                .summaryCost(1000)
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .products(Collections.emptyList())
                .build();
        clientOrderService.update(updateOrder);
        Optional<ClientOrderDto> actualResult = clientOrderService.findById(order.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getSummaryCost()).isEqualTo(1000);
    }

    @Test
    void delete() {
        ClientReadDto user = getUser();
        ClientOrderDto order = clientOrderService.create(user.getId());

        boolean delete = clientOrderService.delete(order.getId());
        Optional<ClientOrderDto> actualResult = clientOrderService.findById(order.getId());

        assertThat(delete).isTrue();
        assertThat(actualResult).isEmpty();
    }

    private ClientReadDto getUser() {
        ClientCreateEditDto clientCreateEditDto = new ClientCreateEditDto("test", "test", "test@gmail.com", "test",
                "89037539183", "test", Role.ADMIN);
        return clientService.create(clientCreateEditDto);
    }

}