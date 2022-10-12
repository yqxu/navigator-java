package com.pingpongx.smb.warning.biz.rules.store;

import com.pingpongx.smb.warning.biz.rules.Rule;
import com.pingpongx.smb.warning.biz.rules.RuleLeaf;

public interface RuleStore {
    void putRule(RuleLeaf rule);
    RuleLeaf getRule(String key);

}
