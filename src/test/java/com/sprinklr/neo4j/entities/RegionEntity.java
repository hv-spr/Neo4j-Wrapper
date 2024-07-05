package com.sprinklr.neo4j.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Node("Region")
public class RegionEntity {
    @Id
    private String regionID;
    private String regionDescription;

    @Setter(AccessLevel.NONE)
    //@Relationship(type = "IN_REGION", direction = Relationship.Direction.INCOMING)
    private Set<TerritoryEntity> territories = new HashSet<>();

    public RegionEntity(String regionID, String regionDescription) {
        this.regionID = regionID;
        this.regionDescription = regionDescription;
    }
}