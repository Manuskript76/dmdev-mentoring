package com.vmdev.eshop.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.QProduct;
import com.vmdev.eshop.util.EntityGraphUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;

import java.util.List;

public class ProductRepository extends RepositoryBase<Long, Product> {
    EntityManager entityManager;

    public ProductRepository(EntityManager entityManager) {
        super(Product.class, entityManager);
        this.entityManager = entityManager;
    }

    public List<Product> findAll(Predicate predicate) {
        RootGraph<Product> entityGraph = EntityGraphUtil.getEntityGraph(
                (Session) entityManager, Product.class, "reviews"
        );
        return new JPAQuery<Product>(entityManager)
                .select(QProduct.product)
                .from(QProduct.product)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), entityGraph)
                .fetch();
    }
}
