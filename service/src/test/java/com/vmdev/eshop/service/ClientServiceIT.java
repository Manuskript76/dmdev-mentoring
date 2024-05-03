package com.vmdev.eshop.service;

import com.vmdev.eshop.dto.ClientCreateEditDto;
import com.vmdev.eshop.dto.ClientReadDto;
import com.vmdev.eshop.entity.enums.Role;
import com.vmdev.eshop.repository.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class ClientServiceIT extends IntegrationTestBase {

    private final ClientService clientService;

    @Test
    void findAll() {
        List<ClientReadDto> actualResult = clientService.findAll();

        assertThat(actualResult).hasSize(5);
    }

    @Test
    void findById() {
        ClientReadDto expectedUser = clientService.create(getUser("test"));

        Optional<ClientReadDto> actualResult = clientService.findById(expectedUser.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(expectedUser.getId());
    }

    @Test
    void findByEmail() {
        ClientReadDto expectedUser = clientService.create(getUser("test"));

        Optional<ClientReadDto> actualResult = clientService.findByEmail(expectedUser.getEmail());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getEmail()).isEqualTo(expectedUser.getEmail());
    }

    @Test
    void create() {
        ClientCreateEditDto expectedUserDto = getUser("test");

        ClientReadDto actualResult = clientService.create(expectedUserDto);

        assertThat(actualResult.getEmail()).isEqualTo(expectedUserDto.getEmail());
    }

    @Test
    void update() {
        ClientReadDto expectedUser = clientService.create(getUser("test"));

        Optional<ClientReadDto> actualResult = clientService.update(expectedUser.getId(), getUser("test2"));

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getFirstname()).isEqualTo("test2");
    }

    @Test
    void delete() {
        ClientReadDto user = clientService.create(getUser("test"));

        boolean delete = clientService.delete(user.getId());
        Optional<ClientReadDto> actualResult = clientService.findById(user.getId());

        assertThat(delete).isTrue();
        assertThat(actualResult).isEmpty();
    }

    @Test
    void loadUserByUsername() {
        ClientReadDto user = clientService.create(getUser("test"));

        UserDetails actualResult = clientService.loadUserByUsername(user.getEmail());
        UserDetails expectedResult = new User(user.getEmail(), "test", Collections.singleton(user.getRole()));

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    private ClientCreateEditDto getUser(String name) {
        return new ClientCreateEditDto(name, "test", "test@gmail.com", "test",
                "89037539183", "test", Role.ADMIN);
    }
}