package com.pingpongx.smb.rule.routers.operatiors.batch;

import com.pingpongx.smb.common.ACTrie;
import com.pingpongx.smb.common.FSMNode;
import com.pingpongx.smb.common.IdentityPath;
import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.rule.routers.operatiors.StringContains;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BatchStrContains implements BatchMatcher<String>{
    ACTrie<Character, MatchedSet> trie = new ACTrie<>();

    Set<Node<RuleLeaf, RuleTrieElement>> notSet = new HashSet<>();

    public void putAndReIndex(String str,Node<RuleLeaf, RuleTrieElement> node,boolean isNot){
        putOnly(str,node,isNot);
        trie.failBackRebuild();
    }

    public void putOnly(String str,Node<RuleLeaf, RuleTrieElement> ruleNode,boolean isNot){
        if (isNot){
            notSet.add(ruleNode);
        }
        //规则入树
        IdentityPath<Character> path = IdentityPath.of(str.toCharArray());
        Node<Character,FSMNode<Character, MatchedSet>> node = trie.getOrCreate(path,FSMNode::new);

        if (node.getData().getData()==null){
            MatchedSet ruleSet = new MatchedSet();
            ruleSet.add(ruleNode,isNot);
            FSMNode<Character, MatchedSet> fsmNode = node.getData();
            fsmNode.setData(ruleSet);
        }else{
            node.getData().getData().add(ruleNode,isNot);
        }
    }

    /****
     * 批量匹配字符串 为StringContains提供批量O（n）支撑
     * @param input 对象的待匹配属性，因为操作符的限制只能是string
     * @return 用了contains 规则的rule 的 identify 集合
     */
    public Set<Node<RuleLeaf, RuleTrieElement>> batchMatch(String input,Set<Node<RuleLeaf, RuleTrieElement>> repeat){
        if (input == null){
            return notSet.stream().collect(Collectors.toSet());
        }
        IdentityPath<Character> path = IdentityPath.of(input.toCharArray());
        MatchedSet matchedSet = trie.walk(path).stream().map(Node::getData).map(FSMNode::getData).filter(Objects::nonNull).reduce((set1, set2)->{
            set1.getMatchedNotRule().addAll(set2.getMatchedNotRule());
            set1.getMatchedRule().addAll(set2.getMatchedRule());
            return set1;
        }).orElse(new MatchedSet());
        return matchedSet.getResult(repeat,notSet);
    }

    @Override
    public MatchOperation supportedOperation() {
        return StringContains.getInstance();
    }

    @Override
    public void putRule(RuleLeaf<?, String> rule, Node<RuleLeaf, RuleTrieElement> node) {
        String exp = rule.expected();
        //TODO:Init 完成节点做一次reindex
        putAndReIndex(exp,node,rule.isNot());
    }

    public static BatchMatcher<String> newInstance() {
        BatchStrContains strContains = new BatchStrContains();
        return strContains;
    }

    @Override
    public String getIdentify() {
        return (String) supportedOperation().getIdentify();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BatchStrContains that = (BatchStrContains) o;

        return getIdentify() != null ? getIdentify().equals(that.getIdentify()) : that.getIdentify() == null;
    }

    @Override
    public int hashCode() {
        return getIdentify() != null ? getIdentify().hashCode() : 0;
    }
}
