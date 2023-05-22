package com.pingpongx.smb.export.module.persistance;

import com.alibaba.fastjson2.annotation.JSONType;

import java.io.Serializable;
import java.util.List;
@JSONType(typeName = "And",typeKey = "ruleType")
public class And extends RuleDto {
    List<RuleDto> andRules;

    public List<RuleDto> getAndRules() {
        return andRules;
    }

    public void setAndRules(List<RuleDto> andRules) {
        this.andRules = andRules;
    }

}
