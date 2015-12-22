package com.arborsoft.neo4j.service;

import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class LiveTemplate extends AbstractTemplate {
    @Autowired
    protected RestGraphDatabase liveGraphDatabase;

    @Autowired
    protected RestCypherQueryEngine liveCypherQueryEngine;

    @PostConstruct
    public void init() {
        super.database = this.liveGraphDatabase;
        super.engine = this.liveCypherQueryEngine;
    }
}
