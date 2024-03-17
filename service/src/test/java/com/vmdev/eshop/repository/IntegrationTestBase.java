package com.vmdev.eshop.repository;

import com.vmdev.eshop.annotation.IT;
import com.vmdev.eshop.util.TestDataImporter;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@IT
@ActiveProfiles("qa")
public abstract class IntegrationTestBase {

    @Autowired
    protected EntityManager entityManager;

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.1");

    @BeforeAll
    static void runContainer() {
        container.start();
    }

    @BeforeEach
    void init() {
        TestDataImporter.importData(entityManager);
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}