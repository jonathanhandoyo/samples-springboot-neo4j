package com.arborsoft.neo4j.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class LiveCatalogService extends AbstractCatalogService {
    protected final Log log = LogFactory.getLog(LiveCatalogService.class);

    @Autowired
    protected LiveTemplate template;

    @PostConstruct
    public void init() {
        super.template = this.template;
    }
}
