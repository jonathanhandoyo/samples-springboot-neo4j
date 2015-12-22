package com.arborsoft.neo4j.controller;

import com.arborsoft.neo4j.service.LiveCatalogService;
import com.arborsoft.neo4j.service.StagingCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Neo4jController {
    @Autowired
    protected StagingCatalogService stagingCatalogService;

    @Autowired
    protected LiveCatalogService liveCatalogService;
}
