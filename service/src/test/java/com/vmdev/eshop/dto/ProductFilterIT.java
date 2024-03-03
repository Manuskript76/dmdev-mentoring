package com.vmdev.eshop.dto;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.Product_;
import com.vmdev.eshop.entity.QProduct;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.filter.QPredicate;
import com.vmdev.eshop.util.HibernateTestUtil;
import com.vmdev.eshop.util.TestDataImporter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductFilterIT {
    private static SessionFactory sessionFactory;
    private Session session;

    @BeforeAll
    static void buildSessionFactory() {
        sessionFactory = HibernateTestUtil.buildSessionFactory();
        TestDataImporter.importData(sessionFactory);
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

    @Nested
    class CriteriaApi {
        @Test
        void getProductByNameFiltering() {
            ProductFilter filter = getFilter("MBR-pr15D", null, null, null);
            RootGraph<Product> graph = getGraph();

            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            JpaCriteriaQuery<Product> criteria = cb.createQuery(Product.class);
            JpaRoot<Product> product = criteria.from(Product.class);
            var predicates = getPredicates(filter, cb, product);
            criteria.select(product).where(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
            List<Product> actualResult = session.createQuery(criteria)
                    .setEntityGraph(graph, GraphSemantic.FETCH)
                    .list();

            actualResult.forEach(products -> products.getReviews().size());
            assertThat(actualResult).hasSize(1);
            assertThat(actualResult.get(0).getName()).isEqualTo(filter.getName());
        }

        @Test
        void getProductsByTypeFiltering() {
            ProductFilter filter = getFilter(null, ProductType.APPLIANCES, null, null);
            RootGraph<Product> graph = getGraph();

            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            JpaCriteriaQuery<Product> criteria = cb.createQuery(Product.class);
            JpaRoot<Product> product = criteria.from(Product.class);
            var predicates = getPredicates(filter, cb, product);
            criteria.select(product).where(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
            List<Product> actualResult = session.createQuery(criteria)
                    .setEntityGraph(graph, GraphSemantic.FETCH)
                    .list();

            actualResult.forEach(products -> products.getReviews().size());
            assertThat(actualResult).hasSize(2);
            actualResult.forEach(products -> assertThat(products.getType()).isSameAs(filter.getType()));
        }

        @Test
        void getProductByManufacturerFiltering() {
            ProductFilter filter = getFilter(null, null, "HUAWEI", null);
            RootGraph<Product> graph = getGraph();

            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            JpaCriteriaQuery<Product> criteria = cb.createQuery(Product.class);
            JpaRoot<Product> product = criteria.from(Product.class);
            var predicates = getPredicates(filter, cb, product);
            criteria.select(product).where(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
            List<Product> actualResult = session.createQuery(criteria)
                    .setEntityGraph(graph, GraphSemantic.FETCH)
                    .list();

            actualResult.forEach(products -> products.getReviews().size());
            assertThat(actualResult).hasSize(1);
            assertThat(actualResult.get(0).getManufacturer()).isEqualTo(filter.getManufacturer());
        }

        @Test
        void getProductsByCostFiltering() {
            ProductFilter filter = getFilter(null, null, null, 70000);
            RootGraph<Product> graph = getGraph();

            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            JpaCriteriaQuery<Product> criteria = cb.createQuery(Product.class);
            JpaRoot<Product> product = criteria.from(Product.class);
            var predicates = getPredicates(filter, cb, product);
            criteria.select(product).where(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
            List<Product> actualResult = session.createQuery(criteria)
                    .setEntityGraph(graph, GraphSemantic.FETCH)
                    .list();

            actualResult.forEach(products -> products.getReviews().size());
            assertThat(actualResult).hasSize(7);
            actualResult.forEach(products -> assertThat(products.getCost()).isLessThan(filter.getCost()));
        }

        @Test
        void getEmptyByWrongFiltering() {
            ProductFilter filter = getFilter(null, null, "AMAZON", null);

            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            JpaCriteriaQuery<Product> criteria = cb.createQuery(Product.class);
            JpaRoot<Product> product = criteria.from(Product.class);
            var predicates = getPredicates(filter, cb, product);
            criteria.select(product).where(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
            List<Product> actualResult = session.createQuery(criteria)
                    .list();

            assertThat(actualResult).isEmpty();
        }

        @Test
        void getAllByEmptyFilters() {
            ProductFilter filter = getFilter(null, null, null, null);
            RootGraph<Product> graph = getGraph();

            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            JpaCriteriaQuery<Product> criteria = cb.createQuery(Product.class);
            JpaRoot<Product> product = criteria.from(Product.class);
            var predicates = getPredicates(filter, cb, product);
            criteria.select(product).where(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
            List<Product> actualResult = session.createQuery(criteria)
                    .setEntityGraph(graph, GraphSemantic.FETCH)
                    .list();

            actualResult.forEach(products -> products.getReviews().size());
            assertThat(actualResult).hasSize(10);
        }

        @Test
        void getProductBySeveralFilters() {
            ProductFilter filter = getFilter("HDR12500D", ProductType.TV, "GIGABYTE", null);
            RootGraph<Product> graph = getGraph();

            HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
            JpaCriteriaQuery<Product> criteria = cb.createQuery(Product.class);
            JpaRoot<Product> product = criteria.from(Product.class);
            var predicates = getPredicates(filter, cb, product);
            criteria.select(product).where(predicates.toArray(jakarta.persistence.criteria.Predicate[]::new));
            List<Product> actualResult = session.createQuery(criteria)
                    .setEntityGraph(graph, GraphSemantic.FETCH)
                    .list();

            actualResult.forEach(products -> products.getReviews().size());
            assertThat(actualResult).hasSize(1);
            assertThat(actualResult.get(0).getName()).isEqualTo(filter.getName());
            assertThat(actualResult.get(0).getType()).isEqualTo(filter.getType());
            assertThat(actualResult.get(0).getManufacturer()).isEqualTo(filter.getManufacturer());
        }
    }

    @Nested
    class QueryDsl {
        @Test
        void getProductByNameFiltering() {
            ProductFilter filter = getFilter("I2P8", null, null, null);
            Predicate predicate = getPredicate(filter);
            RootGraph<Product> productGraph = getGraph();

            List<Product> actualResult = new JPAQuery<Product>(session)
                    .select(QProduct.product)
                    .from(QProduct.product)
                    .where(predicate)
                    .setHint(GraphSemantic.FETCH.getJakartaHintName(), productGraph)
                    .fetch();

            actualResult.forEach(product -> product.getReviews().size());
            assertThat(actualResult).hasSize(1);
            assertThat(actualResult.get(0).getName()).isEqualTo(filter.getName());
        }

        @Test
        void getProductsByTypeFiltering() {
            ProductFilter filter = getFilter(null, ProductType.LAPTOPS, null, null);
            Predicate predicate = getPredicate(filter);
            RootGraph<Product> productGraph = getGraph();

            List<Product> actualResult = new JPAQuery<Product>(session)
                    .select(QProduct.product)
                    .from(QProduct.product)
                    .where(predicate)
                    .setHint(GraphSemantic.FETCH.getJakartaHintName(), productGraph)
                    .fetch();

            actualResult.forEach(product -> product.getReviews().size());
            assertThat(actualResult).hasSize(2);
            actualResult.forEach(product -> assertThat(product.getType()).isSameAs(filter.getType()));
        }

        @Test
        void getProductByManufacturerFiltering() {
            ProductFilter filter = getFilter(null, null, "SAMSUNG", null);
            Predicate predicate = getPredicate(filter);
            RootGraph<Product> productGraph = getGraph();

            List<Product> actualResult = new JPAQuery<Product>(session)
                    .select(QProduct.product)
                    .from(QProduct.product)
                    .where(predicate)
                    .setHint(GraphSemantic.FETCH.getJakartaHintName(), productGraph)
                    .fetch();

            actualResult.forEach(product -> product.getReviews().size());
            assertThat(actualResult).hasSize(1);
            assertThat(actualResult.get(0).getManufacturer()).isEqualTo(filter.getManufacturer());
        }

        @Test
        void getProductsByCostFiltering() {
            ProductFilter filter = getFilter(null, null, null, 70000);
            Predicate predicate = getPredicate(filter);
            RootGraph<Product> productGraph = getGraph();

            List<Product> actualResult = new JPAQuery<Product>(session)
                    .select(QProduct.product)
                    .from(QProduct.product)
                    .where(predicate)
                    .setHint(GraphSemantic.FETCH.getJakartaHintName(), productGraph)
                    .fetch();

            actualResult.forEach(product -> product.getReviews().size());
            assertThat(actualResult).hasSize(7);
            actualResult.forEach(product -> assertThat(product.getCost()).isLessThan(filter.getCost()));
        }

        @Test
        void getEmptyByWrongFiltering() {
            ProductFilter filter = getFilter(null, null, "AMAZON", null);
            Predicate predicate = getPredicate(filter);

            List<Product> actualResult = new JPAQuery<Product>(session)
                    .select(QProduct.product)
                    .from(QProduct.product)
                    .where(predicate)
                    .fetch();

            assertThat(actualResult).isEmpty();
        }

        @Test
        void getAllByEmptyFilters() {
            ProductFilter filter = getFilter(null, null, null, null);
            Predicate predicate = getPredicate(filter);
            RootGraph<Product> productGraph = getGraph();

            List<Product> actualResult = new JPAQuery<Product>(session)
                    .select(QProduct.product)
                    .from(QProduct.product)
                    .where(predicate)
                    .setHint(GraphSemantic.FETCH.getJakartaHintName(), productGraph)
                    .fetch();

            actualResult.forEach(product -> product.getReviews().size());
            assertThat(actualResult).hasSize(10);
        }

        @Test
        void getProductBySeveralFilters() {
            ProductFilter filter = getFilter("X12Omega3", ProductType.LAPTOPS, "ACER", null);
            Predicate predicate = getPredicate(filter);
            RootGraph<Product> productGraph = getGraph();

            List<Product> actualResult = new JPAQuery<Product>(session)
                    .select(QProduct.product)
                    .from(QProduct.product)
                    .where(predicate)
                    .setHint(GraphSemantic.FETCH.getJakartaHintName(), productGraph)
                    .fetch();

            actualResult.forEach(product -> product.getReviews().size());
            assertThat(actualResult).hasSize(1);
            assertThat(actualResult.get(0).getName()).isEqualTo(filter.getName());
            assertThat(actualResult.get(0).getManufacturer()).isEqualTo(filter.getManufacturer());
            assertThat(actualResult.get(0).getType()).isSameAs(filter.getType());
        }
    }

    @NotNull
    private RootGraph<Product> getGraph() {
        RootGraph<Product> productGraph = session.createEntityGraph(Product.class);
        productGraph.addAttributeNodes("reviews");
        return productGraph;
    }

    public Predicate getPredicate(ProductFilter filter) {
        return QPredicate.builder()
                .add(filter.getName(), QProduct.product.name::eq)
                .add(filter.getCost(), QProduct.product.cost::loe)
                .add(filter.getManufacturer(), QProduct.product.manufacturer::eq)
                .add(filter.getType(), QProduct.product.type::eq)
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

    @NotNull
    private static List<jakarta.persistence.criteria.Predicate> getPredicates(ProductFilter filter, HibernateCriteriaBuilder cb, JpaRoot<Product> product) {
        List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
        if (filter.getName() != null) {
            predicates.add(cb.equal(product.get(Product_.name), filter.getName()));
        }
        if (filter.getType() != null) {
            predicates.add(cb.equal(product.get(Product_.type), filter.getType()));
        }
        if (filter.getManufacturer() != null) {
            predicates.add(cb.equal(product.get(Product_.manufacturer), filter.getManufacturer()));
        }
        if (filter.getCost() != null) {
            predicates.add(cb.le(product.get(Product_.cost), filter.getCost()));
        }
        return predicates;
    }
}