package com.vmdev.eshop;

import com.vmdev.eshop.config.TestApplicationConfiguration;
import com.vmdev.eshop.util.TestDataImporter;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public abstract class TestRepositoryBase {

    protected static SessionFactory sessionFactory;
    protected static EntityManager entityManager;
    protected static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void buildSessionFactory() {
        context = new AnnotationConfigApplicationContext(TestApplicationConfiguration.class);
        sessionFactory = context.getBean("SessionFactory", SessionFactory.class);
        TestDataImporter.importData(sessionFactory);
        entityManager = context.getBean("entityManager", EntityManager.class);
    }

    @BeforeEach
    void init() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void close() {
        entityManager.getTransaction().rollback();
        entityManager.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
        context.close();
    }
}
