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
@Node("Territory")
public class TerritoryEntity{
    @Id
    private final String territoryID;
    private final String territoryDescription;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "IN_REGION", direction = Relationship.Direction.OUTGOING)
    private RegionEntity region;

    @Setter(AccessLevel.NONE)
    @Relationship(type = "IN_TERRITORY", direction = Relationship.Direction.INCOMING)
    private Set<EmployeeEntity> employees = new HashSet<>();

    public TerritoryEntity(String territoryID, String territoryDescription) {
        this.territoryID = territoryID;
        this.territoryDescription = territoryDescription;
    }
}