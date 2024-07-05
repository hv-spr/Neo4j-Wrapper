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
@Node("Customer")
public class CustomerEntity{
    @Id
    private String customerID;
    private String contactTitle;
    private String address;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private String contactName;
    private String phone;
    private String fax;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "PURCHASED", direction = Relationship.Direction.OUTGOING)
    private Set<OrderEntity> orders = new HashSet<>();

    public CustomerEntity(String customerID, String country, String contactTitle, String address, String city, String phone, String contactName, String postalCode, String region, String fax) {
        this.customerID = customerID;
        this.country = country;
        this.contactTitle = contactTitle;
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.contactName = contactName;
        this.postalCode = postalCode;
        this.region = region;
        this.fax = fax;
    }
}