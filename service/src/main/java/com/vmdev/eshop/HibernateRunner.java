package com.vmdev.eshop;

import com.vmdev.eshop.entity.Client;
import com.vmdev.eshop.entity.Role;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateRunner {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Client client = Client.builder()
                    .firstname("Ivan")
                    .lastname("Ivanov")
                    .email("ivan@gmail.com")
                    .password("123")
                    .phone("89037028591")
                    .address("Moscow, Aviamotornaya, 10")
                    .role(Role.USER)
                    .build();

//            session.persist(client);

            Client client1 = session.get(Client.class, 1L);
            System.out.println(client1);

            session.getTransaction().commit();
        }
    }
}
