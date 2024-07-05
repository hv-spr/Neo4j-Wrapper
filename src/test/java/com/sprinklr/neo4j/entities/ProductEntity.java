package com.sprinklr.neo4j.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Node("Product")
public class ProductEntity{
    @Id
    private String productID;
    private Double unitPrice;
    private Integer unitsInStock;
    private Integer unitsOnOrder;
    private Integer reorderLevel;
    private Boolean discontinued;
    private String productName;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "ORDERS", direction = Relationship.Direction.INCOMING)
    private Set<OrderEntity> orders = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @Relationship(type = "PART_OF", direction = Relationship.Direction.OUTGOING)
    private Set<CategoryEntity> category = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @Relationship(type = "SUPPLIES", direction = Relationship.Direction.INCOMING)
    private Set<SupplierEntity> suppliers = new HashSet<>();

    public ProductEntity(String productID, Double unitPrice, Integer unitsInStock, Integer unitsOnOrder, Integer reorderLevel, Boolean discontinued, String productName) {
        this.productID = productID;
        this.unitPrice = unitPrice;
        this.unitsInStock = unitsInStock;
        this.unitsOnOrder = unitsOnOrder;
        this.reorderLevel = reorderLevel;
        this.discontinued = discontinued;
        this.productName = productName;
    }
}