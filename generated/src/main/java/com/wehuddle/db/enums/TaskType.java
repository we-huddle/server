/*
 * This file is generated by jOOQ.
 */
package com.wehuddle.db.enums;


import com.wehuddle.db.Public;

import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public enum TaskType implements EnumType {

    QUIZ("QUIZ"),

    DEV("DEV");

    private final String literal;

    private TaskType(String literal) {
        this.literal = literal;
    }

    @Override
    public Catalog getCatalog() {
        return getSchema() == null ? null : getSchema().getCatalog();
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public String getName() {
        return "task_type";
    }

    @Override
    public String getLiteral() {
        return literal;
    }
}
