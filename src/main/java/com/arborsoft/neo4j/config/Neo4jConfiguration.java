package com.arborsoft.neo4j.config;

import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.arborsoft.neo4j"})
public class Neo4jConfiguration {

    private RestGraphDatabase stagingGraphDatabase;
    private RestCypherQueryEngine stagingCypherQueryEngine;

    private RestGraphDatabase liveGraphDatabase;
    private RestCypherQueryEngine liveCypherQueryEngine;

    @Bean
    public RestGraphDatabase stagingGraphDatabase() {
        String restUri = "http://10.90.76.226:6475/db/data/";
        String username = null;
        String password = null;

        this.stagingGraphDatabase = new RestGraphDatabase(restUri, username, password);
        return this.stagingGraphDatabase;
    }

    @Bean
    public RestGraphDatabase liveGraphDatabase() {
        String restUri = "http://10.90.76.226:6474/db/data/";
        String username = null;
        String password = null;

        this.liveGraphDatabase = new RestGraphDatabase(restUri, username, password);
        return this.liveGraphDatabase;
    }

    @Bean
    RestCypherQueryEngine stagingCypherQueryEngine() {
        if (this.stagingCypherQueryEngine == null) {
            this.stagingCypherQueryEngine = new RestCypherQueryEngine(this.stagingGraphDatabase.getRestAPI());
        }
        return this.stagingCypherQueryEngine;
    }

    @Bean
    RestCypherQueryEngine liveCypherQueryEngine() {
        if (this.liveCypherQueryEngine == null) {
            this.liveCypherQueryEngine = new RestCypherQueryEngine(this.liveGraphDatabase.getRestAPI());
        }
        return this.liveCypherQueryEngine;
    }
}
