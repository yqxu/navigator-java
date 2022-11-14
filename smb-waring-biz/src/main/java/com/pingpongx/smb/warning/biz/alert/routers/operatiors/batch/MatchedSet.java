package com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch;

import java.util.HashSet;
import java.util.Set;

public class MatchedSet {
    Set<String> matchedRule = new HashSet<>();
    Set<String> matchedNotRule = new HashSet<>();

    public Set<String> getMatchedRule() {
        return matchedRule;
    }

    public void setMatchedRule(Set<String> matchedRule) {
        this.matchedRule = matchedRule;
    }

    public Set<String> getMatchedNotRule() {
        return matchedNotRule;
    }

    public void setMatchedNotRule(Set<String> matchedNotRule) {
        this.matchedNotRule = matchedNotRule;
    }

    void add(String ruleIdentify,boolean isNot){
        if (isNot){
            matchedNotRule.add(ruleIdentify);
            return;
        }
        matchedRule.add(ruleIdentify);
    }
}
