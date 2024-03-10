package com.vmdev.eshop.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.QProduct;
import com.vmdev.eshop.util.EntityGraphUtil;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository extends RepositoryBase<Long, Product> {

    public ProductRepository(EntityManager entityManager) {
        super(Product.class, entityManager);
    }

    public List<Product> findAll(Predicate predicate) {
        EntityGraph<Product> entityGraph = EntityGraphUtil.getEntityGraph(
                entityManager, Product.class, "reviews"
        );
        return new JPAQuery<Product>(entityManager)
                .select(QProduct.product)
                .from(QProduct.product)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), entityGraph)
                .fetch();
    }
}
