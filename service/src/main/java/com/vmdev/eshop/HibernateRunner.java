package com.vmdev.eshop;

import com.vmdev.eshop.entity.Client;
import com.vmdev.eshop.entity.ClientOrder;
import com.vmdev.eshop.entity.OrderProduct;
import com.vmdev.eshop.entity.Review;
import com.vmdev.eshop.entity.enums.OrderStatus;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.entity.enums.ReviewGrade;
import com.vmdev.eshop.entity.enums.Role;
import com.vmdev.eshop.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.time.LocalDate;

public class HibernateRunner {
    public static void main(String[] args) {
        Client client = Client.builder()
                .firstname("Ivan")
                .lastname("Ivanov")
                .email("ivan@gmail.com")
                .password("123")
                .phone("89037028591")
                .address("Moscow, Aviamotornaya, 10")
                .role(Role.USER)
                .build();

        ClientOrder clientOrder = ClientOrder.builder()
                .openDate(LocalDate.now())
                .client(client)
                .status(OrderStatus.IN_PROGRESS)
                .build();

        Product product = Product.builder()
                .name("TV")
                .cost(23000)
                .quantity(10)
                .type(ProductType.TV)
                .manufacturer("LG")
                .build();

        OrderProduct orderProduct = OrderProduct.builder()
                .product(product)
                .clientOrder(clientOrder)
                .quantity(1)
                .build();

        Review review1 = Review.builder()
                .client(client)
                .product(product)
                .review("good TV!")
                .date(LocalDate.now())
                .grade(ReviewGrade.VERY_GOOD)
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();

//            session.persist(client);
//            session.persist(product);
//            session.persist(clientOrder);
//            session.persist(orderProduct);
            Client client1 = session.get(Client.class, 1L);
            ClientOrder clientOrder1 = session.get(ClientOrder.class, 1L);
            Product product1 = session.get(Product.class, 1L);
            OrderProduct orderProduct1 = session.get(OrderProduct.class, 1L);
            Review review = session.get(Review.class, 1L);

//            session.persist(review);
//            System.out.println();

            session.getTransaction().commit();
        }
    }
}
