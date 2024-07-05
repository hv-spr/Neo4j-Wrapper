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
@Node("Supplier")
public class SupplierEntity {
    @Id
    private String supplierID;
    private String companyName;
    private String contactName;
    private String contactTitle;
    private String address;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private String phone;
    private String fax;
    private String homePage;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "SUPPLIES", direction = Relationship.Direction.OUTGOING)
    private Set<ProductEntity> products = new HashSet<>();

    public SupplierEntity(String supplierID, String companyName, String contactName, String contactTitle, String address, String city, String region, String postalCode, String country, String phone, String fax, String homePage) {
        this.supplierID = supplierID;
        this.companyName = companyName;
        this.contactName = contactName;
        this.contactTitle = contactTitle;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.phone = phone;
        this.fax = fax;
        this.homePage = homePage;
    }
}