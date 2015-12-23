package com.arborsoft.neo4j.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class StagingCatalogService extends AbstractCatalogService {
    @Autowired
    protected StagingTemplate template;

    @PostConstruct
    public void init() {
        super.template = this.template;
    }
}
