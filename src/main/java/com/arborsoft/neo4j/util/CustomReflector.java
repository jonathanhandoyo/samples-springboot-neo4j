package com.arborsoft.neo4j.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class CustomReflector {
    public static List<Field> getFields(Class<?> _class, boolean editable) {
        Class<?> current = _class;
        List<Field> all = new ArrayList<>();
        while (current != null) {
            for (Field field : current.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isTransient(field.getModifiers())) {
                    if (editable) {
                        field.setAccessible(true);
                    }
                    all.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return all;
    }
}
