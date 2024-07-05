package com.sprinklr.neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.core.mapping.Neo4jMappingContext;

@Configuration
@PropertySource("classpath:application.properties")
public class Neo4jConfig {

    @Value("${neo4j.uri}")
    private String uri;

    @Value("${neo4j.authentication.username}")
    private String username;

    @Value("${neo4j.authentication.password}")
    private String password;

    @Bean
    public Driver neo4jDriver() {
        return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }

    @Bean
    public Session neo4jSession(Driver driver) {
        return driver.session();
    }

    @Bean
    public Neo4jClient neo4jClient() {
        return Neo4jClient.create(neo4jDriver());
    }

    @Bean
    public Neo4jTemplate neo4jTemplate(Neo4jClient neo4jClient, Neo4jMappingContext neo4jMappingContext){
        return new Neo4jTemplate(neo4jClient, neo4jMappingContext);
    }

    @Bean
    public Neo4jWrapper neo4jWrapper(Neo4jClient neo4jClient, Neo4jMappingContext neo4jMappingContext, Neo4jTemplate neo4jTemplate){
        return new Neo4jWrapper(neo4jClient, neo4jMappingContext, neo4jTemplate);
    }
}
