package com.vmdev.eshop.dto;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.QProduct;
import com.vmdev.eshop.entity.enums.ProductType;
import com.vmdev.eshop.filter.QPredicate;
import com.vmdev.eshop.util.HibernateTestUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
class ProductFilterTest {

    Product tv1 = getProduct("tv1", ProductType.TV, "LG", 35000);
    Product tv2 = getProduct("tv2", ProductType.TV, "SAMSUNG", 69000);
    Product laptop1 = getProduct("laptop1", ProductType.LAPTOPS, "HP", 50000);
    Product laptop2 = getProduct("laptop2", ProductType.LAPTOPS, "MSI", 110000);
    Product phone = getProduct("iphone", ProductType.PHONES, "APPLE", 120000);

    @Test
    void checkFilter() {
        ProductFilter filter = getFilter("tv1", null, null, null);
        Predicate predicate = getPredicate(filter);
        @Cleanup SessionFactory sessionFactory = HibernateTestUtil.buildSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();

        session.getTransaction();
        session.persist(tv1);
        session.persist(tv2);
        session.persist(laptop1);
        session.persist(laptop2);
        session.persist(phone);
        session.flush();
        List<Product> fetch = new JPAQuery<Product>(session)
                .select(QProduct.product)
                .from(QProduct.product)
                .where(predicate)
                .fetch();

        fetch.forEach(System.out::println);
        session.getTransaction().rollback();
    }

    public Product getProduct(String name, ProductType type, String manufacturer, Integer cost) {
        return Product.builder()
                .name(name)
                .type(type)
                .manufacturer(manufacturer)
                .cost(cost)
                .quantity(1)
                .build();
    }

    public Predicate getPredicate(ProductFilter filter) {
        return QPredicate.builder()
                .add(filter.getName(), QProduct.product.name::eq)
                .add(filter.getCost(), QProduct.product.cost::lt)
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
}