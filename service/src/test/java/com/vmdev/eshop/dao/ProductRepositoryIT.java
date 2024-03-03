package com.vmdev.eshop.dao;

import com.querydsl.core.types.Predicate;
import com.vmdev.eshop.dto.ProductFilter;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.QProduct;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.filter.QPredicate;
import com.vmdev.eshop.util.HibernateTestUtil;
import com.vmdev.eshop.util.SessionProxy;
import com.vmdev.eshop.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductRepositoryIT {

    private static SessionFactory sessionFactory;
    private Session session;
    private ProductRepository productRepository;

    @BeforeAll
    static void buildSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.importData(sessionFactory);
    }

    @BeforeEach
    void init() {
        session = SessionProxy.getSession(sessionFactory);
        productRepository = new ProductRepository(session);
        session.beginTransaction();
    }

    @AfterEach
    void close() {
        session.getTransaction().rollback();
        session.close();
    }

    @AfterAll
    static void closeSessionFactory() {
        sessionFactory.close();
    }

    @Test
    void findProductById() {
        Product expectedResult = session.get(Product.class, 1L);
        Optional<Product> actualResult = productRepository.findById(1L);

        assertThat(actualResult).isPresent();
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void findNothingIfProductNotExist() {
        Optional<Product> actualResult = productRepository.findById(100000L);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void findAllProductsCheck() {
        List<Product> actualResult = productRepository.findAll();

        assertThat(actualResult).hasSize(10);
    }

    @Test
    void createProductCheck() {
        Product product = Product.builder()
                .name("TV SAMSUNG V7")
                .type(ProductType.TV)
                .manufacturer("SAMSUNG")
                .cost(37990)
                .quantity(6)
                .build();

        productRepository.save(product);
        Optional<Product> actualResult = productRepository.findById(product.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getId()).isEqualTo(product.getId());
    }

    @Test
    void deleteProductCheck() {
        Product product = Product.builder()
                .name("TV SAMSUNG V7")
                .type(ProductType.TV)
                .manufacturer("SAMSUNG")
                .cost(37990)
                .quantity(6)
                .build();

        productRepository.save(product);
        productRepository.delete(product);
        Optional<Product> actualResult = productRepository.findById(product.getId());

        assertThat(actualResult).isEmpty();
    }

    @Test
    void updateProductCheck() {
        Product product = Product.builder()
                .name("TV SAMSUNG V7")
                .type(ProductType.TV)
                .manufacturer("SAMSUNG")
                .cost(37990)
                .quantity(6)
                .build();
        product.setCost(32500);

        productRepository.save(product);
        productRepository.update(product);
        session.flush();
        Optional<Product> actualResult = productRepository.findById(product.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getCost()).isEqualTo(32500);
    }

    @Test
    void findAllProductsWithNameFilter() {
        ProductFilter filter = getFilter("MBR-pr15D", null, null, null);
        Predicate predicate = getPredicate(filter);
        List<Product> actualResult = productRepository.findAll(predicate);

        actualResult.forEach(product -> assertThat(product.getName()).isEqualTo(filter.getName()));
    }

    @Test
    void findAllProductsWithManufacturerFilter() {
        ProductFilter filter = getFilter(null, null, "PHILIPS", null);
        Predicate predicate = getPredicate(filter);
        List<Product> actualResult = productRepository.findAll(predicate);

        actualResult.forEach(product -> assertThat(product.getManufacturer()).isEqualTo(filter.getManufacturer()));
    }

    @Test
    void findAllProductsWithCostFilter() {
        ProductFilter filter = getFilter(null, null, null, 70000);
        Predicate predicate = getPredicate(filter);
        List<Product> actualResult = productRepository.findAll(predicate);

        actualResult.forEach(product -> assertThat(product.getCost()).isLessThan(filter.getCost()));
    }

    @Test
    void findAllProductsWithTypeFilter() {
        ProductFilter filter = getFilter(null, ProductType.TV, null, null);
        Predicate predicate = getPredicate(filter);
        List<Product> actualResult = productRepository.findAll(predicate);

        actualResult.forEach(product -> assertThat(product.getType()).isSameAs(filter.getType()));
    }

    @Test
    void findAllClientsWithEmptyFilter() {
        ProductFilter filter = getFilter(null, null, null, null);
        Predicate predicate = getPredicate(filter);
        List<Product> actualResult = productRepository.findAll(predicate);

        assertThat(actualResult).hasSize(10);
    }

    @Test
    void findNoClientsWithWrongFilter() {
        ProductFilter filter = getFilter(null, null, "null", null);
        Predicate predicate = getPredicate(filter);
        List<Product> actualResult = productRepository.findAll(predicate);

        assertThat(actualResult).isEmpty();
    }

    @Test
    void findClientsWithSeveralFilters() {
        ProductFilter filter = getFilter("MBR-pr15D", ProductType.OFFICE, "PHILIPS", 10000);
        Predicate predicate = getPredicate(filter);
        List<Product> actualResult = productRepository.findAll(predicate);

        assertThat(actualResult).hasSize(1);
    }

    public Predicate getPredicate(ProductFilter filter) {
        return QPredicate.builder()
                .add(filter.getName(), QProduct.product.name::eq)
                .add(filter.getType(), QProduct.product.type::eq)
                .add(filter.getManufacturer(), QProduct.product.manufacturer::eq)
                .add(filter.getCost(), QProduct.product.cost::loe)
                .buildAnd();
    }

    public ProductFilter getFilter(String name, ProductType type, String manufacturer, Integer cost) {
        return ProductFilter.builder()
                .name(name)
                .type(type)
                .manufacturer(manufacturer)
                .cost(cost)
                .build();
    }
}