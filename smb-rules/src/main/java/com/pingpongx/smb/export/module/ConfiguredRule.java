package com.pingpongx.smb.export.module;

import com.alibaba.fastjson2.JSON;
import com.pingpongx.smb.export.module.operation.RuleLeaf;

public abstract class ConfiguredRule<T> extends RuleLeaf<T> {
    String type;
    String attr;
    MatchOperation operation;
    String expected;
    boolean not;

    @Override
    public String dependsObject() {
        return type;
    }

    @Override
    public String dependsAttr() {
        return attr;
    }

    @Override
    public MatchOperation operatorType() {
        return operation;
    }

    @Override
    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }


    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public MatchOperation getOperation() {
        return operation;
    }

    public void setOperation(MatchOperation operation) {
        this.operation = operation;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }

}
