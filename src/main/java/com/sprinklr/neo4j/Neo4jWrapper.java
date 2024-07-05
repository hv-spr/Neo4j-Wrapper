package com.sprinklr.neo4j;

import org.neo4j.driver.internal.value.NodeValue;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.TypeSystem;
import org.neo4j.driver.types.MapAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Neo4jWrapper {
    private final Neo4jClient neo4jClient;
    private final Neo4jMappingContext neo4jMappingContext;
    private final Neo4jTemplate neo4jTemplate;

    @Autowired
    public Neo4jWrapper(Neo4jClient neo4jClient, Neo4jMappingContext neo4jMappingContext, Neo4jTemplate neo4jTemplate){
        this.neo4jClient = neo4jClient;
        this.neo4jMappingContext = neo4jMappingContext;
        this.neo4jTemplate = neo4jTemplate;
    }

    @Transactional("transactionManager")
    public void executeQuery(String query) {neo4jClient.query(query).run();}

    @Transactional("transactionManager")
    public <T> List<T> executeQuery(String query, Class<T> type) {
        if (neo4jMappingContext.hasPersistentEntityFor(type)) {
            return new ArrayList<>(neo4jClient.query(query)
                    .fetchAs(type)
                    .mappedBy(neo4jMappingContext.getRequiredMappingFunctionFor(type)::apply)
                    .all());
        }
        else {
            return new ArrayList<>(neo4jClient.query(query)
                    .fetchAs(type)
                    .mappedBy((typeSystem, record) -> customMap(typeSystem, record, type))
                    .all());
        }
    }

    private <T> T customMap(TypeSystem typeSystem, MapAccessor record, Class<T> type) {
        try {
            if (record.size() == 1) {
                Object singleValue = record.values().iterator().next();
                if (singleValue instanceof NodeValue) return mapNodeToEntity(((NodeValue) singleValue).asNode(), type);
            } else {
                T customInstance = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (record.containsKey(fieldName)) {
                        Object value = record.get(fieldName).asObject();
                        if (value instanceof Node) {
                            if (neo4jMappingContext.hasPersistentEntityFor(field.getType())) {
                                value = neo4jMappingContext.getRequiredMappingFunctionFor(field.getType()).apply(typeSystem, (Node) value);
                            } else {
                                value = mapNodeToEntity((Node) value, field.getType());
                            }
                        } else if (value instanceof List) {
                            value = mapCollectToList((List<?>) value, field);
                        }
                        field.set(customInstance, value);
                    }
                }
                return customInstance;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map query result to class: " + type.getName(), e);
        }
    }

    private <T> T mapNodeToEntity(Node node, Class<T> entityClass) {
        try {
            T entityInstance = entityClass.getDeclaredConstructor().newInstance();
            for (Field field : entityClass.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (node.containsKey(fieldName)) {
                    Object value = node.get(fieldName).asObject();
                    field.set(entityInstance, value);
                }
            }
            return entityInstance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map Node to entity class: " + entityClass.getName(), e);
        }
    }

    private Object mapCollectToList(List<?> list, Field field) throws Exception {
        if (list.isEmpty()) return list;
        Class<?> listType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        if (Node.class.isAssignableFrom(list.get(0).getClass())) {
            return list.stream()
                    .map(node -> {
                        if (neo4jMappingContext.hasPersistentEntityFor(listType)) {
                            return neo4jMappingContext.getRequiredMappingFunctionFor(listType).apply(null, (Node) node);
                        } else {
                            return mapNodeToEntity((Node) node, listType);
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            return list;
        }
    }

    @Transactional("transactionManager")
    public <T> void save(T entity) {
        if (neo4jMappingContext.hasPersistentEntityFor(entity.getClass())) {
            neo4jTemplate.save(entity);
        } else {
            handleCustomSave(entity);
        }
    }

    @Transactional("transactionManager")
    private <T> void handleCustomSave(T entity) {
        Map<String, Object> properties = new HashMap<>();

        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                properties.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }

        neo4jClient.query(buildCreateQuery(entity.getClass().getSimpleName(), properties))
                .bindAll(properties)
                .run();
    }

    private String buildCreateQuery(String label, Map<String, Object> properties) {
        StringBuilder queryBuilder = new StringBuilder("CREATE (n:" + label + " {");

        for (String key : properties.keySet()) queryBuilder.append(key).append(": $").append(key).append(", ");

        if (!properties.isEmpty()) queryBuilder.setLength(queryBuilder.length() - 2);

        queryBuilder.append("})");

        return queryBuilder.toString();
    }
}
