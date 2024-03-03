package com.vmdev.eshop.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.vmdev.eshop.entity.Client;
import com.vmdev.eshop.entity.Product;
import com.vmdev.eshop.entity.QClient;
import com.vmdev.eshop.util.EntityGraphUtil;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;

import java.util.List;

public class ClientRepository extends RepositoryBase<Long, Client> {
    EntityManager entityManager;

    public ClientRepository(EntityManager entityManager) {
        super(Client.class, entityManager);
        this.entityManager = entityManager;
    }

    public List<Client> findAll(Predicate predicate) {
        RootGraph<Client> entityGraph = EntityGraphUtil.getEntityGraph(
                (Session) entityManager, Client.class, "orders"
        );
        return new JPAQuery<Product>(entityManager)
                .select(QClient.client)
                .from(QClient.client)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJakartaHintName(), entityGraph)
                .fetch();
    }
}
