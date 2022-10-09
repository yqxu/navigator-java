package com.pingpongx.smb.warning.biz.rules.store;

import com.pingpongx.smb.warning.biz.rules.Rule;
import com.pingpongx.smb.warning.biz.rules.RuleLeaf;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemoryRuleStore implements RuleStore {
    Map<String, RuleLeaf> map = new ConcurrentHashMap<>();
    @Override
    public void putRule(RuleLeaf rule) {
        map.put(rule.getIdentify(),rule);
    }

    @Override
    public RuleLeaf getRule(String key) {
        return map.get(key);
    }

}
