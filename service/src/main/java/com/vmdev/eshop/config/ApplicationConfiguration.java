package com.vmdev.eshop.config;

import com.vmdev.eshop.util.HibernateUtil;
import com.vmdev.eshop.util.SessionProxy;
import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.vmdev.eshop")
public class ApplicationConfiguration {

    @Bean
    public SessionFactory SessionFactory() {
        return HibernateUtil.buildSessionFactory();
    }

    @Bean
    public EntityManager entityManager(SessionFactory sessionFactory) {
        return SessionProxy.getSession(sessionFactory);
    }
}
