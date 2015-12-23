package com.arborsoft.neo4j.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StagingTemplate extends AbstractTemplate {
    private final Log log = LogFactory.getLog(StagingTemplate.class);

    @Autowired
    protected RestGraphDatabase stagingGraphDatabase;

    @Autowired
    protected RestCypherQueryEngine stagingCypherQueryEngine;

    @PostConstruct
    public void init() {
        super.database = this.stagingGraphDatabase;
        super.engine = this.stagingCypherQueryEngine;
    }
}
