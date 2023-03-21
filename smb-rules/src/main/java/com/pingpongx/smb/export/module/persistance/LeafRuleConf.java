package com.pingpongx.smb.export.module.persistance;

import com.alibaba.fastjson2.annotation.JSONType;

import java.io.Serializable;

@JSONType(typeName = "Leaf",typeKey = "ruleType")
public class LeafRuleConf extends RuleDto {
    String ruleType = "Leaf";
    String type;
    String attr;
    String operation;
    String expected;
    boolean not = false;

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
}
