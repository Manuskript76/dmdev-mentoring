package com.vmdev.eshop.repository;

import com.querydsl.core.types.Predicate;
import com.vmdev.eshop.TestRepositoryBase;
import com.vmdev.eshop.dto.ClientFilter;
import com.vmdev.eshop.entity.Client;
import com.vmdev.eshop.entity.QClient;
import com.vmdev.eshop.entity.enums.Role;
import com.vmdev.eshop.filter.QPredicate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientRepositoryIT extends TestRepositoryBase {

    private static ClientRepository clientRepository;
    private final Long CLIENT_ID = 1L;

    @BeforeAll
    static void init() {
        clientRepository = context.getBean("clientRepository", ClientRepository.class);
    }

    @Test
    void findClientById() {
        Client expectedResult = entityManager.find(Client.class, CLIENT_ID);
        Optional<Client> actualResult = clientRepository.findById(CLIENT_ID);

        assertThat(actualResult).isPresent();
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void findNothingIfClientNotExist() {
        Optional<Client> actualResult = clientRepository.findById(10000000L);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void findAllClientsCheck() {
        List<Client> actualResult = clientRepository.findAll();

        assertThat(actualResult).hasSize(5);
    }

    @Test
    void createClientCheck() {
        Client client = Client.builder()
                .firstname("Kar")
                .lastname("Karov")
                .password("q31dh6?")
                .phone("+72430734230")
                .email("test@gmail.com")
                .address("test")
                .role(Role.ADMIN)
                .build();

        clientRepository.save(client);
        Optional<Client> actualResult = clientRepository.findById(client.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getEmail()).isEqualTo(client.getEmail());
    }

    @Test
    void deleteClientCheck() {
        Client client = Client.builder()
                .firstname("Kar")
                .lastname("Karov")
                .password("q31dh6?")
                .phone("+72430734230")
                .email("test@gmail.com")
                .address("test")
                .role(Role.ADMIN)
                .build();

        clientRepository.save(client);
        clientRepository.delete(client);
        Optional<Client> actualResult = clientRepository.findById(client.getId());

        assertThat(actualResult).isEmpty();
    }

    @Test
    void updateClientCheck() {
        Client client = Client.builder()
                .firstname("Kar")
                .lastname("Karov")
                .password("q31dh6?")
                .phone("+72430734230")
                .email("test@gmail.com")
                .address("test")
                .role(Role.ADMIN)
                .build();
        client.setEmail("testUpdated@gmail.com");

        clientRepository.save(client);
        clientRepository.update(client);
        entityManager.flush();
        Optional<Client> actualResult = clientRepository.findById(client.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getEmail()).isEqualTo("testUpdated@gmail.com");
    }

    @Test
    void findAllClientsWithFirstnameFilter() {
        ClientFilter filter = getFilter("Ivan", null, null, null, null, null);
        Predicate predicate = getPredicate(filter);
        List<Client> actualResult = clientRepository.findAll(predicate);

        actualResult.forEach(client -> assertThat(client.getFirstname()).isEqualTo(filter.getFirstName()));
    }

    @Test
    void findAllClientsWithLastnameFilter() {
        ClientFilter filter = getFilter(null, "Ivanov", null, null, null, null);
        Predicate predicate = getPredicate(filter);
        List<Client> actualResult = clientRepository.findAll(predicate);

        actualResult.forEach(client -> assertThat(client.getLastname()).isEqualTo(filter.getLastName()));
    }

    @Test
    void findClientWithEmailFilter() {
        ClientFilter filter = getFilter(null, null, "vanya@gmail.com", null, null, null);
        Predicate predicate = getPredicate(filter);
        List<Client> actualResult = clientRepository.findAll(predicate);

        assertThat(actualResult.get(0).getEmail()).isEqualTo(filter.getEmail());
    }

    @Test
    void findClientWithPhoneFilter() {
        ClientFilter filter = getFilter(null, null, null, "+79034219402", null, null);
        Predicate predicate = getPredicate(filter);
        List<Client> actualResult = clientRepository.findAll(predicate);

        assertThat(actualResult.get(0).getPhone()).isEqualTo(filter.getPhone());
    }

    @Test
    void findAllClientsWithAddressFilter() {
        ClientFilter filter = getFilter(null, null, null, null, "Moscow", null);
        Predicate predicate = getPredicate(filter);
        List<Client> actualResult = clientRepository.findAll(predicate);

        actualResult.forEach(client -> assertThat(client.getAddress()).isEqualTo(filter.getAddress()));
    }

    @Test
    void findAllClientsWithRoleFilter() {
        ClientFilter filter = getFilter(null, null, null, null, null, Role.USER);
        Predicate predicate = getPredicate(filter);
        List<Client> actualResult = clientRepository.findAll(predicate);

        actualResult.forEach(client -> assertThat(client.getRole()).isSameAs(filter.getRole()));
    }

    @Test
    void findAllClientsWithEmptyFilter() {
        ClientFilter filter = getFilter(null, null, null, null, null, null);
        Predicate predicate = getPredicate(filter);
        List<Client> actualResult = clientRepository.findAll(predicate);

        assertThat(actualResult).hasSize(5);
    }

    @Test
    void findNoClientsWithWrongFilter() {
        ClientFilter filter = getFilter(null, "null", null, null, null, null);
        Predicate predicate = getPredicate(filter);
        List<Client> actualResult = clientRepository.findAll(predicate);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void findClientsWithSeveralFilters() {
        ClientFilter filter = getFilter("Andrey", "Andreev", "drus@gmail.com",
                "+79033079405", "Vatikan", Role.ADMIN);
        Predicate predicate = getPredicate(filter);
        List<Client> actualResult = clientRepository.findAll(predicate);

        assertThat(actualResult).hasSize(1);
    }

    public Predicate getPredicate(ClientFilter filter) {
        return QPredicate.builder()
                .add(filter.getFirstName(), QClient.client.firstname::eq)
                .add(filter.getLastName(), QClient.client.lastname::eq)
                .add(filter.getEmail(), QClient.client.email::eq)
                .add(filter.getPhone(), QClient.client.phone::eq)
                .add(filter.getAddress(), QClient.client.address::eq)
                .add(filter.getRole(), QClient.client.role::eq)
                .buildAnd();
    }

    public ClientFilter getFilter(String firstname, String lastname, String email, String phone, String address, Role role) {
        return ClientFilter.builder()
                .firstName(firstname)
                .lastName(lastname)
                .email(email)
                .phone(phone)
                .address(address)
                .role(role)
                .build();
    }
}