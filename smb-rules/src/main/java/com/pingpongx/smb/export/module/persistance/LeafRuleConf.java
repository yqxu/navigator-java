package com.pingpongx.smb.export.module.persistance;

import com.alibaba.fastjson2.annotation.JSONType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JSONType(typeName = "Leaf",typeKey = "ruleType")
public class LeafRuleConf extends RuleDto {
    String type;
    String attr;
    String operation;
    String expected;
    boolean not = false;

    List<String> debugHandlerCodes = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public boolean isNot() {
        return not;
    }

    public void setNot(boolean not) {
        this.not = not;
    }

    public boolean isNeedDebug() {
        return debugHandlerCodes.isEmpty();
    }

    public List<String> getDebugHandlerCodes() {
        return debugHandlerCodes;
    }

    public void setDebugHandlerCodes(List<String> debugHandlerCodes) {
        this.debugHandlerCodes = debugHandlerCodes;
    }
}
