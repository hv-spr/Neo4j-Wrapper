package com.sprinklr.neo4j.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Node("Employee")
public class EmployeeEntity{
    @Id
    private String employeeID;
    private String title;
    private String titleOfCourtesy;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String hireDate;
    private String address;
    private String city;
    private String region;
    private String postalCode;
    private String country;
    private String homePhone;
    private String extension;
    private String photoPath;
    private String notes;

    @Setter(AccessLevel.NONE)
    @Relationship(value = "IN_TERRITORY", direction = Relationship.Direction.OUTGOING)
    private Set<TerritoryEntity> territories = new HashSet<>();

    @Setter(AccessLevel.NONE)
    @Relationship(value = "REPORTS_TO", direction = Relationship.Direction.OUTGOING)
    private EmployeeEntity reportsTo;

    @Setter(AccessLevel.NONE)
    @Relationship(value = "SOLD", direction = Relationship.Direction.OUTGOING)
    private Set<OrderEntity> orders = new HashSet<>();

    public EmployeeEntity(String employeeID, String title, String titleOfCourtesy, String firstName, String lastName, String birthDate, String hireDate, String address, String city, String region, String postalCode, String country, String homePhone, String extension, String photoPath, String notes) {
        this.employeeID = employeeID;
        this.title = title;
        this.titleOfCourtesy = titleOfCourtesy;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.hireDate = hireDate;
        this.address = address;
        this.city = city;
        this.region = region;
        this.postalCode = postalCode;
        this.country = country;
        this.homePhone = homePhone;
        this.extension = extension;
        this.photoPath = photoPath;
        this.notes = notes;
    }
}