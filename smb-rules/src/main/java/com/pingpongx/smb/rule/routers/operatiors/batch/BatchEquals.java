package com.pingpongx.smb.rule.routers.operatiors.batch;

import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public abstract class BatchEquals<ValType> implements SameTypeMatcher<ValType> {

    Map<ValType, MatchedSet> ruleMap = new ConcurrentHashMap<>();

    Set<Node<RuleLeaf, RuleTrieElement>> notSet = new HashSet<>();

    public void putOnly(ValType val, Node<RuleLeaf, RuleTrieElement> node, boolean isNot) {
        if (isNot) {
            notSet.add(node);
        }
        MatchedSet ret = ruleMap.get(val);
        if (ret == null) {
            ret = new MatchedSet();
            ruleMap.put(val, ret);
        }
        ret.add(node, isNot);
    }

    /****
     * 批量匹配字符串 为StringContains提供批量O（n）支撑
     * @param input 对象的待匹配属性，因为操作符的限制只能是string
     * @return 用了contains 规则的rule 的 identify 集合
     */
    public Set<Node<RuleLeaf, RuleTrieElement>> batchMatch(Object input, Set<Node<RuleLeaf, RuleTrieElement>> repeat) {
        if (input == null) {
            return notSet.stream().collect(Collectors.toSet());
        }
        MatchedSet matchedSet = Optional.ofNullable(ruleMap.get(input)).orElse(new MatchedSet());
        return matchedSet.getResult(repeat, notSet);
    }

    @Override
    public void putRule(RuleLeaf<ValType> rule, Node<RuleLeaf, RuleTrieElement> node) {
        ValType exp = rule.expected();
        putOnly(exp, node, rule.isNot());
    }

    @Override
    public String getIdentify() {
        return (String) supportedOperation().getIdentify();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BatchEquals that = (BatchEquals) o;

        return getIdentify() != null ? getIdentify().equals(that.getIdentify()) : that.getIdentify() == null;
    }

    @Override
    public int hashCode() {
        return getIdentify() != null ? getIdentify().hashCode() : 0;
    }
}
