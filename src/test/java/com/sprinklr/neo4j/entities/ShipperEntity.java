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
@Node("Shipper")
public class ShipperEntity {
    @Id
    private final String shipperID;
    private final String companyName;
    private final String phone;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "SHIPS", direction = Relationship.Direction.OUTGOING)
    private Set<OrderEntity> orders = new HashSet<>();

    public ShipperEntity(String shipperID, String companyName, String phone) {
        this.shipperID = shipperID;
        this.companyName = companyName;
        this.phone = phone;
    }
}