package com.vmdev.eshop.util;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.experimental.UtilityClass;
import org.hibernate.graph.GraphSemantic;

import java.util.Map;

@UtilityClass
public class EntityGraphUtil {

    public <T> Map<String, Object> getProperties(EntityManager entityManager, Class<T> entity, String... nodes) {
        EntityGraph<T> entityGraph = entityManager.createEntityGraph(entity);
        entityGraph.addAttributeNodes(nodes);
        return Map.of(GraphSemantic.FETCH.getJakartaHintName(), entityGraph);
    }

    public <T> EntityGraph<T> getEntityGraph(EntityManager entityManager, Class<T> entity, String... nodes) {
        EntityGraph<T> entityGraph = entityManager.createEntityGraph(entity);
        entityGraph.addAttributeNodes(nodes);
        return entityGraph;
    }

}
