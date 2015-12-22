package com.arborsoft.neo4j.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends BaseNode {
    protected String stockCode;
}
