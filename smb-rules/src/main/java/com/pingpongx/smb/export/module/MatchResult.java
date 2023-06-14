package com.pingpongx.smb.export.module;

import com.pingpongx.smb.export.spi.RuleHandler;

import java.util.Set;

public class MatchResult {
    Set<RuleHandler> matchedData;

    public Set<RuleHandler> getMatchedData() {
        return matchedData;
    }

    public void setMatchedData(Set<RuleHandler> matchedData) {
        this.matchedData = matchedData;
    }


}
