package com.arborsoft.neo4j.service;

import com.arborsoft.neo4j.domain.BaseNode;
import com.arborsoft.neo4j.domain.Product;
import com.arborsoft.neo4j.util.CustomReflector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    public void wipe() {
        this.engine.query("MATCH (n) WITH n OPTIONAL MATCH (n) -[r]- () DELETE n;", null);
        this.engine.query("MATCH (n) DELETE n;", null);
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
}
