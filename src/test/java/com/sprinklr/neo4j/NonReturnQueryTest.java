package com.sprinklr.neo4j;

import com.sprinklr.neo4j.entities.RegionEntity;

import lombok.Getter;
import lombok.Setter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NonReturnQueryTest {

    @Autowired
    private Neo4jWrapper neo4jWrapper;

    @Getter
    @Setter
    static class ExtendedRegionEntity{
        private RegionEntity regionEntity;
        private Long created_prop;
    }

    @Test
    @Transactional
    public void queryWithNoReturn() {
        neo4jWrapper.save(new RegionEntity("temp_id", "temp_description"));
        neo4jWrapper.executeQuery("MATCH (r:Region where r.regionID = \"temp_id\") SET r.created_prop = 1;");
        ExtendedRegionEntity test_region = neo4jWrapper.executeQuery("MATCH (r:Region where r.regionID = \"temp_id\") RETURN r as regionEntity, r.created_prop as created_prop;", ExtendedRegionEntity.class).get(0);
        assertThat(test_region.getRegionEntity().getRegionID()).isEqualTo("temp_id");
        assertThat(test_region.getCreated_prop()).isEqualTo(1);
    }
}
