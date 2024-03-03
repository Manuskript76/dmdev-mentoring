package com.vmdev.eshop.util;

import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;

import java.util.Map;

@UtilityClass
public class EntityGraphUtil {

    public <T> Map<String, Object> getProperties(Session session, Class<T> entity, String... nodes) {
        RootGraph<T> entityGraph = session.createEntityGraph(entity);
        entityGraph.addAttributeNodes(nodes);
        return Map.of(GraphSemantic.LOAD.getJakartaHintName(), entityGraph);
    }

    public <T> RootGraph<T> getEntityGraph(Session session, Class<T> entity, String... nodes) {
        RootGraph<T> entityGraph = session.createEntityGraph(entity);
        entityGraph.addAttributeNodes(nodes);
        return entityGraph;
    }

}
