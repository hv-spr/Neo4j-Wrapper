package com.sprinklr.neo4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ConnectionTest {

    @Autowired
    private Neo4jClient neo4jClient;

    @Test
    @Transactional
    public void testNeo4jConnection() {
        Integer result = neo4jClient.query("RETURN 1").fetchAs(Integer.class).one().orElse(null);
        assertThat(result).isEqualTo(1);
    }
}
