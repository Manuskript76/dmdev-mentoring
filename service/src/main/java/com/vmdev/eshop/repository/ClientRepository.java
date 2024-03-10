package com.vmdev.eshop.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vmdev.eshop.entity.Client;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.QClient;
import com.vmdev.eshop.util.EntityGraphUtil;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientRepository extends RepositoryBase<Long, Client> {

    public ClientRepository(EntityManager entityManager) {
        super(Client.class, entityManager);
    }

    public List<Client> findAll(Predicate predicate) {
        EntityGraph<Client> entityGraph = EntityGraphUtil.getEntityGraph(
                entityManager, Client.class, "orders"
        );
        return new JPAQuery<Product>(entityManager)
                .select(QClient.client)
                .from(QClient.client)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), entityGraph)
                .fetch();
    }
}
