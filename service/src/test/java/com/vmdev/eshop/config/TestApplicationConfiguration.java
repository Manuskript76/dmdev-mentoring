package com.vmdev.eshop.config;

import com.vmdev.eshop.util.HibernateTestUtil;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ApplicationConfiguration.class)
public class TestApplicationConfiguration {

    @Bean
    public SessionFactory SessionFactory() {
        return HibernateTestUtil.buildSessionFactory();
    }
}
