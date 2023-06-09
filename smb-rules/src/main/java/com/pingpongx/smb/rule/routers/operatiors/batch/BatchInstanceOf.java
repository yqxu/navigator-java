package com.pingpongx.smb.rule.routers.operatiors.batch;


import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BatchInstanceOf implements BatchMatcher<Object,String> {

    private MatchOperation operation;
    Map<String, MatchedSet> ruleMap = new ConcurrentHashMap<>();

    Set<Node<RuleLeaf, RuleTrieElement>> notSet = new HashSet<>();

    public void putOnly(String val, Node<RuleLeaf, RuleTrieElement> node, boolean isNot) {
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
     * 批量匹配字符串 为StringContains提供批量O（1）支撑
     * @param input 对象的待匹配属性，因为操作符的限制只能是string
     * @return 用了contains 规则的rule 的 identify 集合
     */
    public Set<Node<RuleLeaf, RuleTrieElement>> batchMatch(Object input, Set<Node<RuleLeaf, RuleTrieElement>> repeat) {
        if (input == null) {
            return notSet.stream().collect(Collectors.toSet());
        }
        String in = input.getClass().getSimpleName();
        MatchedSet matchedSet = Optional.ofNullable(ruleMap.get(in)).orElse(new MatchedSet());
        return matchedSet.getResult(repeat, notSet);
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

    @Override
    public MatchOperation supportedOperation() {
        return this.operation;
    }
    @Override
    public void supportedOperation(MatchOperation operation) {
        this.operation = operation;
    }

    @Override
    public void putRule(RuleLeaf<String> rule, Node<RuleLeaf, RuleTrieElement> node) {
        String exp = rule.expected();
        putOnly(exp, node, rule.isNot());
    }

    public static BatchMatcher<Object,String> newInstance() {
        BatchInstanceOf instanceOf = new BatchInstanceOf();
        return instanceOf;
    }
}
