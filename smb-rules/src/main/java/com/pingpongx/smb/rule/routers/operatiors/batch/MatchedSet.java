package com.pingpongx.smb.rule.routers.operatiors.batch;

import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MatchedSet {
    Set<Node<RuleLeaf, RuleTrieElement>> matchedRule = new HashSet<>();
    Set<Node<RuleLeaf, RuleTrieElement>> matchedNotRule = new HashSet<>();

    public Set<Node<RuleLeaf, RuleTrieElement>> getMatchedRule() {
        return matchedRule;
    }

    public void setMatchedRule(Set<Node<RuleLeaf, RuleTrieElement>> matchedRule) {
        this.matchedRule = matchedRule;
    }

    public Set<Node<RuleLeaf, RuleTrieElement>> getMatchedNotRule() {
        return matchedNotRule;
    }

    public void setMatchedNotRule(Set<Node<RuleLeaf, RuleTrieElement>> matchedNotRule) {
        this.matchedNotRule = matchedNotRule;
    }

    void add(Node<RuleLeaf, RuleTrieElement> node,boolean isNot){
        if (isNot){
            matchedNotRule.add(node);
            return;
        }
        matchedRule.add(node);
    }

    Set<Node<RuleLeaf, RuleTrieElement>> getResult(Set<Node<RuleLeaf, RuleTrieElement>> parentRepeat,Set<Node<RuleLeaf, RuleTrieElement>> notSet){
        if (notSet == null){
            notSet = new HashSet<>();
        }
        Set<Node<RuleLeaf, RuleTrieElement>> ret = Stream.concat(getMatchedRule().stream(),notSet.stream()
                        .filter(s -> !matchedNotRule.contains(s))).filter(node->node.getParent() != null)
                //实现且逻辑短路优化
                .filter(node -> node.getParent().getParent()==null||parentRepeat.contains(node.getParent()))
                .collect(Collectors.toSet());
        return ret;
    }
}
