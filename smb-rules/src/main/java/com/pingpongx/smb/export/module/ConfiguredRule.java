package com.pingpongx.smb.export.module;

import com.alibaba.fastjson2.JSON;
import com.pingpongx.smb.export.module.operation.RuleLeaf;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfiguredRule<T> extends RuleLeaf<T> {
    String type;
    String attr;
    MatchOperation operation;
    String expected;
    boolean not;

    List<String> debugHandlerCodes;

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

    public boolean needDebug() {
        boolean ret = debugHandlerCodes==null || debugHandlerCodes.isEmpty();
        return !ret;
    }

    public List<String> getDebugHandlerCodes() {
        return debugHandlerCodes;
    }

    public void setDebugHandlerCodes(List<String> debugHandlerCodes) {
        this.debugHandlerCodes = debugHandlerCodes;
    }
}
