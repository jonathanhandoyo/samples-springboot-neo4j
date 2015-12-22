package com.arborsoft.neo4j.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseNode extends BaseDomain {
    protected String code;
    protected String name;
}
