package com.sprinklr.neo4j;

import com.sprinklr.neo4j.entities.EmployeeEntity;

import com.sprinklr.neo4j.entities.OrderEntity;
import com.sprinklr.neo4j.entities.ProductEntity;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class SimpleAggregationTest {

    @Autowired
    private Neo4jWrapper neo4jWrapper;

    @Setter
    @Getter
    static class ExampleAggregate {
        private OrderEntity orderEntity;
        private ProductEntity productEntity;
    }

    @Test
    public void aggregateOrderProduct() {
        List<ExampleAggregate> exampleAggregates = neo4jWrapper.executeQuery("MATCH (o:Order where o.orderID = \"10248\")-[:ORDERS]->(p:Product) RETURN o as orderEntity, p as productEntity;", ExampleAggregate.class);

        for (ExampleAggregate exampleAggregate : exampleAggregates) {
            assertThat(exampleAggregate.getOrderEntity().getOrderID()).isNotNull();
            assertThat(exampleAggregate.getProductEntity().getProductID()).isNotNull();
        }
    }
}
