package com.arborsoft.neo4j.test;

import com.arborsoft.neo4j.Application;
import com.arborsoft.neo4j.config.Neo4jConfiguration;
import com.arborsoft.neo4j.domain.Product;
import com.arborsoft.neo4j.service.StagingCatalogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration(classes = {Application.class, Neo4jConfiguration.class})
public class MainTest {
    @Autowired
    private StagingCatalogService stagingCatalogService;

    @Test
    public void test1() throws Exception {
        Product product1 = new Product();
        product1.setCode("code1");
        product1.setName("name1");
        product1.setStockCode("stock-code1");
        this.stagingCatalogService.create(product1);
    }
}
