package com.pingpongx.smb.export.module.persistance;

import com.alibaba.fastjson2.annotation.JSONType;

import java.io.Serializable;
@JSONType(seeAlso = {And.class, Or.class , LeafRuleConf.class},typeKey = "ruleType2")
public abstract class RuleDto implements Serializable {
}
