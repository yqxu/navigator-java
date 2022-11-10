package com.pingpongx.smb.warning.biz.rules.store;

import com.pingpongx.smb.warning.biz.rules.Rule;
import com.pingpongx.smb.warning.biz.rules.RuleLeaf;

/****
 * TODO:维护 rule identify 到 Rule 的映射，用于规则逆推，优化现有的 bfsGet 逻辑
 * com.pingpongx.smb.warning.biz.moudle.Trie#bfsGet(com.pingpongx.smb.warning.biz.moudle.IdentityPath)
 */
public interface RuleStore {
    void putRule(RuleLeaf rule);
    RuleLeaf getRule(String key);

}
