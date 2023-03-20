package com.pingpongx.smb.rule.routers.operatiors.batch;

import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class BatchNotEmpty<ValType> implements SameTypeMatcher<ValType> {

    Set<Node<RuleLeaf, RuleTrieElement>> emptySet = new HashSet<>();
    Set<Node<RuleLeaf, RuleTrieElement>> notEmptySet = new HashSet<>();

    public void putOnly(Node<RuleLeaf, RuleTrieElement> node, boolean isNot) {
        if (isNot) {
            emptySet.add(node);
        } else {
            notEmptySet.add(node);
        }

    }

    /****
     * 批量匹配字符串 为StringContains提供批量O（n）支撑
     * @param input 对象的待匹配属性，因为操作符的限制只能是string
     * @return 用了contains 规则的rule 的 identify 集合
     */
    public Set<Node<RuleLeaf, RuleTrieElement>> batchMatch(Object input, Set<Node<RuleLeaf, RuleTrieElement>> repeat) {
        if (input == null) {
            return emptySet.stream().collect(Collectors.toSet());
        } else {
            return notEmptySet.stream().collect(Collectors.toSet());
        }
    }

    @Override
    public void putRule(RuleLeaf<ValType> rule, Node<RuleLeaf, RuleTrieElement> node) {
        putOnly(node, rule.isNot());
    }

    @Override
    public String getIdentify() {
        return (String) supportedOperation().getIdentify();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BatchNotEmpty that = (BatchNotEmpty) o;

        return getIdentify() != null ? getIdentify().equals(that.getIdentify()) : that.getIdentify() == null;
    }

    @Override
    public int hashCode() {
        return getIdentify() != null ? getIdentify().hashCode() : 0;
    }
}
