package com.arborsoft.neo4j.service;

import com.arborsoft.neo4j.converter.NodeConverter;
import com.arborsoft.neo4j.domain.BaseNode;
import lombok.Getter;
import org.neo4j.cypherdsl.grammar.Execute;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;

import java.util.Map;

public abstract class AbstractTemplate {
    @Getter
    protected RestGraphDatabase database;

    @Getter
    protected RestCypherQueryEngine engine;

    public <N extends BaseNode> N getById(Class<N> type, long id) {
        return new NodeConverter<N>().map(this.database.getNodeById(id));
    }

    public QueryResult<Map<String, Object>> query(Execute executable, Map<String, Object> parameter) {
        return this.engine.query(executable.toString(), parameter);
    }
}
