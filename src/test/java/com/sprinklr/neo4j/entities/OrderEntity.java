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
@Node("Order")
public class OrderEntity {
    @Id
    private String orderID;
    private String shipCity;
    private String freight;
    private String requiredDate;
    private String shipName;
    private String shipPostalCode;
    private String orderDate;
    private String shippedDate;
    private String shipRegion;
    private String shipAddress;
    private String shipCountry;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "orders", direction = Relationship.Direction.OUTGOING)
    private Set<ProductEntity> products = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @Relationship(type = "PURCHASED", direction = Relationship.Direction.INCOMING)
    private CustomerEntity customer;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "SHIPS", direction = Relationship.Direction.INCOMING)
    private ShipperEntity shipper;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "SOLD", direction = Relationship.Direction.INCOMING)
    private EmployeeEntity employee;

    public OrderEntity(String orderID, String shipCity, String freight, String requiredDate, String shipName, String shipPostalCode, String orderDate, String shippedDate, String shipRegion, String shipAddress, String shipCountry) {
        this.orderID = orderID;
        this.shipCity = shipCity;
        this.freight = freight;
        this.requiredDate = requiredDate;
        this.shipName = shipName;
        this.shipPostalCode = shipPostalCode;
        this.orderDate = orderDate;
        this.shippedDate = shippedDate;
        this.shipRegion = shipRegion;
        this.shipAddress = shipAddress;
        this.shipCountry = shipCountry;
    }
}