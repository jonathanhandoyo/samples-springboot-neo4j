package com.arborsoft.neo4j.service;

import com.arborsoft.neo4j.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

@Service
public class StagingCatalogService extends AbstractCatalogService {
    @Autowired
    protected StagingTemplate template;

    @PostConstruct
    public void init() {
        super.template = this.template;
    }

    public void create(Product product) throws Exception {
        Assert.isNull(product.getId());

        this.template.save(product);
    }
}
