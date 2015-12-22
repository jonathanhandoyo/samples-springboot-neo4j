package com.arborsoft.neo4j.service;

import com.arborsoft.neo4j.converter.NodeConverter;
import com.arborsoft.neo4j.domain.Product;
import org.neo4j.cypherdsl.CypherQuery;
import org.neo4j.cypherdsl.grammar.Execute;
import org.neo4j.graphdb.Node;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.neo4j.cypherdsl.CypherQuery.identifier;
import static org.neo4j.cypherdsl.CypherQuery.node;

public class AbstractCatalogService {
    protected AbstractTemplate template;

    public Set<Product> getProducts() {
        Execute query = CypherQuery
                .match(node("product"))
                .returns(identifier("product"));

        return StreamSupport.stream(this.template.query(query, null).spliterator(), false).map((it) -> new NodeConverter<Product>().map((Node) it.get("product"))).collect(Collectors.toSet());
    }
}
