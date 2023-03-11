package com.pingpongx.smb.export.module.persistance;


import com.alibaba.fastjson2.annotation.JSONType;
import com.pingpongx.smb.export.module.Rule;

import java.io.Serializable;
import java.util.List;

@JSONType(typeName = "Or")
public class Or extends RuleDto {
    List<RuleDto> orRules;

    public List<RuleDto> getOrRules() {
        return orRules;
    }

    public void setOrRules(List<RuleDto> orRules) {
        this.orRules = orRules;
    }
}
