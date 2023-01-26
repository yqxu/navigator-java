package com.pingpongx.smb.export.module.persistance;

import java.io.Serializable;
import java.util.List;

public class And implements Serializable {
    List<LeafRuleConf> andRules;

    public List<LeafRuleConf> getAndRules() {
        return andRules;
    }

    public void setAndRules(List<LeafRuleConf> andRules) {
        this.andRules = andRules;
    }
}
