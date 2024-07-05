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
@Node("Category")
public class CategoryEntity{
    @Id
    private String categoryID;
    private String categoryName;
    private String description;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "PART_OF", direction = Relationship.Direction.INCOMING)
    private Set<ProductEntity> products = new HashSet<>();

    public CategoryEntity(String categoryID, String categoryName, String description) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.description = description;
    }
}