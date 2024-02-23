package com.vmdev.eshop;

import com.vmdev.eshop.entity.Client;
import com.vmdev.eshop.entity.ClientOrder;
import com.vmdev.eshop.entity.OrderProduct;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.Review;
import com.vmdev.eshop.entity.enums.OrderStatus;
import com.vmdev.eshop.entity.enums.ReviewGrade;
import com.vmdev.eshop.entity.enums.Role;
import com.vmdev.eshop.util.HibernateTestUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class EntityCrudIT {

    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    static void buildSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @BeforeEach
    void init() {
        session = sessionFactory.openSession();
        session.beginTransaction();
    }

    @AfterEach
    void close() {
        session.getTransaction().rollback();
        session.close();
    }

    @Test
    void successCreateGetClient() {
        Client client = getClient();

        session.persist(client);
        session.flush();
        session.clear();
        Client actualResult = session.get(Client.class, client.getId());

        assertNotNull(actualResult);
    }

    @Test
    void obtainingNullWhenGetClientIfClientNotExists() {
        Client actualResult = session.get(Client.class, 123456789L);

        assertNull(actualResult);
    }

    @Test
    void successUpdateClient() {
        Client client = getClient();

        session.persist(client);
        session.flush();
        session.clear();
        client.setEmail("updated@gmail.com");

        Client actualResult = session.merge(client);
        assertThat(actualResult.getEmail()).isEqualTo("updated@gmail.com");
    }

    @Test
    void successDeleteClient() {
        Client client = getClient();

        session.persist(client);
        session.flush();
        session.clear();
        Client dbClient = session.get(Client.class, client.getId());
        session.remove(dbClient);
        session.flush();
        Client actualResult = session.get(Client.class, client.getId());

        assertNull(actualResult);
    }

    @Test
    void successCreateGetClientOrder() {
        Client client = getClient();
        ClientOrder clientOrder = getClientOrder(client);

        session.persist(client);
        session.persist(clientOrder);
        session.flush();
        session.clear();
        ClientOrder actualResult = session.get(ClientOrder.class, clientOrder.getId());

        assertNotNull(actualResult);
        assertThat(actualResult.getClient()).isEqualTo(client);
    }

    @Test
    void successUpdateClientOrder() {
        Client client = getClient();
        ClientOrder clientOrder = getClientOrder(client);

        session.persist(client);
        session.persist(clientOrder);
        session.flush();
        session.clear();
        clientOrder.setStatus(OrderStatus.DECLINED);
        session.merge(clientOrder);
        Client actualResult = session.get(Client.class, client.getId());

        assertThat(actualResult.getOrders().get(0).getStatus()).isSameAs(OrderStatus.DECLINED);
    }

    @Test
    void successDeleteClientOrder() {
        Client client = getClient();
        ClientOrder clientOrder = getClientOrder(client);

        session.persist(client);
        session.persist(clientOrder);
        session.flush();
        session.clear();
        Client dbClient = session.get(Client.class, client.getId());
        session.remove(dbClient.getOrders().get(0));
        session.flush();
        session.clear();
        Client actualResult = session.get(Client.class, client.getId());

        assertThat(actualResult.getOrders()).isEmpty();
    }

    @Test
    void successCreateGetProduct() {
        Product product = getProduct();

        session.persist(product);
        session.flush();
        session.clear();
        Product actualResult = session.get(Product.class, product.getId());

        assertNotNull(actualResult);
    }

    @Test
    void successUpdateProduct() {
        Product product = getProduct();

        session.persist(product);
        session.flush();
        session.clear();
        product.setCost(31900);
        session.merge(product);
        session.flush();
        session.clear();
        Product actualResult = session.get(Product.class, product.getId());

        assertThat(actualResult.getCost()).isEqualTo(31900);
    }

    @Test
    void successDeleteProduct() {
        Product product = getProduct();

        session.persist(product);
        session.flush();
        session.clear();
        Product dbProduct = session.get(Product.class, product.getId());
        session.remove(dbProduct);
        session.flush();
        session.clear();
        Product actualResult = session.get(Product.class, product.getId());

        assertNull(actualResult);
    }

    @Test
    void successCreateGetOrderProduct() {
        Client client = getClient();
        ClientOrder clientOrder = getClientOrder(client);
        Product product = getProduct();
        OrderProduct orderProduct = getOrderProduct(product, clientOrder);

        session.persist(client);
        session.persist(clientOrder);
        session.persist(product);
        session.persist(orderProduct);
        session.flush();
        session.clear();
        Client actualResult = session.get(Client.class, client.getId());
        OrderProduct dbOrderProduct = session.get(OrderProduct.class, orderProduct.getId());

        assertNotNull(actualResult.getOrders().get(0).getProducts().get(0));
        assertThat(dbOrderProduct.getClientOrder().getId()).isEqualTo(clientOrder.getId());
    }

    @Test
    void successUpdateOrderProduct() {
        Client client = getClient();
        ClientOrder clientOrder = getClientOrder(client);
        Product product1 = getProduct();
        Product product2 = getProduct();
        product2.setName("other");
        OrderProduct orderProduct = getOrderProduct(product1, clientOrder);

        session.persist(client);
        session.persist(clientOrder);
        session.persist(product1);
        session.persist(product2);
        session.persist(orderProduct);
        session.flush();
        session.clear();
        orderProduct.setProduct(product2);
        session.merge(orderProduct);
        session.flush();
        session.clear();
        Client actualResult = session.get(Client.class, client.getId());

        assertThat(actualResult.getOrders().get(0).getProducts().get(0).getProduct().getName()).isEqualTo("other");
    }

    @Test
    void successDeleteOrderProduct() {
        Client client = getClient();
        ClientOrder clientOrder = getClientOrder(client);
        Product product1 = getProduct();
        OrderProduct orderProduct = getOrderProduct(product1, clientOrder);

        session.persist(client);
        session.persist(clientOrder);
        session.persist(product1);
        session.persist(orderProduct);
        session.flush();
        session.clear();
        OrderProduct dbOrderProduct = session.get(OrderProduct.class, orderProduct.getId());
        session.remove(dbOrderProduct);
        session.flush();
        session.clear();
        Client actualResult = session.get(Client.class, client.getId());

        assertThat(actualResult.getOrders().get(0).getProducts()).isEmpty();
    }

    @Test
    void successCreateGetReview() {
        Client client = getClient();
        Product product = getProduct();
        Review review = getReview(client, product);

        session.persist(client);
        session.persist(product);
        session.persist(review);
        session.flush();
        session.clear();
        Client actualResult = session.get(Client.class, client.getId());
        Review actualReview = session.get(Review.class, review.getId());

        assertNotNull(actualResult.getReviews());
        assertThat(actualReview.getClient()).isEqualTo(client);
        assertThat(actualReview.getProduct()).isEqualTo(product);
    }

    @Test
    void successUpdateReview() {
        Client client = getClient();
        Product product = getProduct();
        Review review = getReview(client, product);

        session.persist(client);
        session.persist(product);
        session.persist(review);
        session.flush();
        session.clear();
        review.setGrade(ReviewGrade.EXCELLENT);
        session.merge(review);
        session.flush();
        session.clear();
        Client actualResult = session.get(Client.class, client.getId());

        assertThat(actualResult.getReviews().get(0).getGrade()).isSameAs(ReviewGrade.EXCELLENT);
    }

    @Test
    void ClientOrderAddProductMethodTest() {
        Client client = getClient();
        ClientOrder clientOrder = getClientOrder(client);
        Product product = getProduct();
        OrderProduct orderProduct = getOrderProduct(product, clientOrder);
        clientOrder.addProduct(orderProduct);

        session.persist(client);
        session.persist(product);
        session.persist(clientOrder);
        session.persist(orderProduct);
        session.flush();
        session.clear();
        Client actualResult = session.get(Client.class, client.getId());

        assertThat(actualResult.getOrders().get(0).getProducts().get(0).getProduct()).isEqualTo(orderProduct.getProduct());
    }

    private static Client getClient() {
        return Client.builder()
                .firstname("Ivan")
                .lastname("Ivanov")
                .email("Ivan@gmail.com")
                .password("123")
                .phone("123123123")
                .address("Moscow")
                .role(Role.USER)
                .build();
    }

    private static ClientOrder getClientOrder(Client client) {
        return ClientOrder.builder()
                .openDate(LocalDate.now())
                .status(OrderStatus.IN_PROGRESS)
                .client(client)
                .build();
    }

    private static OrderProduct getOrderProduct(Product product, ClientOrder order) {
        OrderProduct orderProduct = OrderProduct.builder()
                .clientOrder(order)
                .quantity(1)
                .build();
        orderProduct.addProduct(product);
        return orderProduct;
    }

    private static Product getProduct() {
        return Product.builder()
                .name("GBJ5040")
                .cost(35000)
                .quantity(15)
                .build();
    }

    private static Review getReview(Client client, Product product) {
        Review review = Review.builder()
                .client(client)
                .product(product)
                .review("Good TV!")
                .date(LocalDate.now())
                .grade(ReviewGrade.GOOD)
                .build();
        review.setClient(client);
        review.setProduct(product);
        return review;
    }

}