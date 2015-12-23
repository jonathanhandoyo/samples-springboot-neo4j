package com.arborsoft.neo4j.service;

import com.arborsoft.neo4j.converter.NodeConverter;
import com.arborsoft.neo4j.domain.BaseNode;
import com.arborsoft.neo4j.util.CustomMap;
import com.arborsoft.neo4j.util.CustomReflector;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.cypherdsl.grammar.Execute;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Node;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class AbstractTemplate {
    private final Log log = LogFactory.getLog(StagingTemplate.class);

    @Getter
    protected RestGraphDatabase database;

    @Getter
    protected RestCypherQueryEngine engine;

    public void wipe() {
        this.engine.query("MATCH (n) WITH n OPTIONAL MATCH (n) -[r]- () DELETE r, n;", null);
        this.engine.query("MATCH (n) DELETE n;", null);
    }

    public <N extends BaseNode> N getById(Class<N> type, long id) {
        return new NodeConverter<N>().map(this.database.getNodeById(id));
    }

    public QueryResult<Map<String, Object>> query(Execute executable, Map<String, Object> parameter) {
        return this.engine.query(executable.toString(), parameter);
    }

    public <N extends BaseNode> N save(N node) throws Exception {
        try {
            Assert.notNull(node);

            Node _node = (node.getId() != null ? this.database.getNodeById(node.getId()) : null);
            if (_node == null) {
                _node = this.database.createNode(DynamicLabel.label(node.getClass().getSimpleName()));
                Assert.notNull(_node);
                Assert.notNull(_node.getId());

                node.setId(_node.getId());
            }

            for (Field field: CustomReflector.getFields(node.getClass(), true)) {
                if (field.get(node) != null) {
                    if (Map.class.isAssignableFrom(field.getType())) {
                        try {
                            _node.setProperty(field.getName(), new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(field.get(node)));
                        } catch (Exception e) {
                            log.error("unable to save JSON string representation of Map for Node[" + node.getId() + "] on key[" + field.getName() + "]");
                        }
                    }
                    _node.setProperty(field.getName(), field.get(node));
                } else {
                    _node.removeProperty(field.getName());
                }
            }

            return node;
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public <N extends BaseNode> void delete(N node) throws Exception {
        Assert.notNull(node);
        Assert.notNull(node.getId());

        this.engine.query("START n = node({id}) OPTIONAL MATCH (n) -[r]- () DELETE r, n;", CustomMap.map(CustomMap.entry("id", node.getId())));
    }
}
