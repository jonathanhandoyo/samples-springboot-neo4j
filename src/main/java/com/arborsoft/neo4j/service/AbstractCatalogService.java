package com.arborsoft.neo4j.service;

import com.arborsoft.neo4j.converter.NodeConverter;
import com.arborsoft.neo4j.domain.BaseNode;
import com.arborsoft.neo4j.domain.Product;
import com.arborsoft.neo4j.util.CustomMap;
import org.neo4j.cypherdsl.CypherQuery;
import org.neo4j.cypherdsl.grammar.Execute;
import org.neo4j.graphdb.Node;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.neo4j.cypherdsl.CypherQuery.*;

public class AbstractCatalogService {
    protected AbstractTemplate template;

    public <N extends BaseNode> Set<N> getAll(Class<N> type) {
        Execute query = CypherQuery
                .match(node("n").label(type.getSimpleName()))
                .returns(identifier("n"));

        return StreamSupport.stream(this.template.query(query, null).spliterator(), false).map((it) -> new NodeConverter<N>().map((Node) it.get("n"))).collect(Collectors.toSet());
    }

    public <N extends BaseNode> N get(Class<N> type, Long id) {
        return this.template.getById(type, id);
    }

    public <N extends BaseNode> void create(N node) throws Exception {
        Assert.isNull(node.getId());

        this.template.save(node);
    }

    public <N extends BaseNode> void update(N node) throws Exception {
        Assert.notNull(node.getId());

        this.template.save(node);
    }

    public <N extends BaseNode> void delete(N node) throws Exception {
        Assert.notNull(node.getId());

        this.template.delete(node);
    }
}
