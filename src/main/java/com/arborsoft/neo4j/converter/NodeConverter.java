package com.arborsoft.neo4j.converter;

import com.arborsoft.neo4j.domain.BaseNode;
import org.neo4j.graphdb.Node;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Date;

public class NodeConverter<N extends BaseNode> {
    public N map(Node node) {
        try {
            Assert.notNull(node);

            Class<?> captured = Class.forName("com.arborsoft.neo4j.domain." + node.getProperty("__type__"));
            N result = (N) captured.newInstance();

            for (String key: node.getPropertyKeys()) {
                if ("__labels__".equals(key)) continue;

                Field field = ReflectionUtils.findField(captured, key);
                if (field == null) continue;

                if (field.getType().equals(Date.class)) {
                    field.set(result, new Date((long) node.getProperty(key)));
                } else if (field.getType().isEnum()) {
                    field.set(result, Enum.valueOf(field.getType().asSubclass(Enum.class), node.getProperty(key).toString()));
                } else if (Number.class.isAssignableFrom(field.getType())) {
                    field.set(result, field.getType().getMethod("valueOf", String.class).invoke(field, node.getProperty(key).toString()));
                } else if (field.getType().equals(Class.class)) {
                    field.set(result, Class.forName((String) node.getProperty(key)));
                } else {
                    field.set(result, node.getProperty(key));
                }

                result.setId(node.getId());
            }

            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
