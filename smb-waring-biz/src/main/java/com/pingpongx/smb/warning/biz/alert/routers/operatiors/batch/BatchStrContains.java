package com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.moudle.*;
import com.pingpongx.smb.warning.biz.rules.RuleLeaf;

import java.util.*;
import java.util.stream.Collectors;

public class BatchStrContains implements BatchMatcher<String>{
    ACTrie<Character, MatchedSet> trie = new ACTrie<>();

    Set<String> notSet = new HashSet<>();

    public void putAndReIndex(String str,String ruleIdentify,boolean isNot){
        putOnly(str,ruleIdentify,isNot);
        trie.failBackRebuild();
    }

    public void putOnly(String str,String ruleIdentify,boolean isNot){
        if (isNot){
            notSet.add(ruleIdentify);
        }
        //规则入树
        IdentityPath<Character> path = IdentityPath.of(str.toCharArray());
        Node<Character,FSMNode<Character, MatchedSet>> node = trie.getOrCreate(path,FSMNode::new);

        if (node.getData().getData()==null){
            MatchedSet ruleSet = new MatchedSet();
            ruleSet.add(ruleIdentify,isNot);
            FSMNode<Character, MatchedSet> fsmNode = node.getData();
            fsmNode.setData(ruleSet);
        }else{
            node.getData().getData().add(ruleIdentify,isNot);
        }
    }

    /****
     * 批量匹配字符串 为StringContains提供批量O（n）支撑
     * @param input 对象的待匹配属性，因为操作符的限制只能是string
     * @return 用了contains 规则的rule 的 identify 集合
     */
    public Set<String> batchMatch(String input){
        IdentityPath<Character> path = IdentityPath.of(input.toCharArray());
        MatchedSet matchedSet = trie.walk(path).stream().map(Node::getData).map(FSMNode::getData).filter(Objects::nonNull).reduce((set1,set2)->{
            set1.getMatchedRule().addAll(set2.getMatchedRule());
            set1.getMatchedNotRule().addAll(set2.getMatchedNotRule());
            return set1;
        }).orElse(new MatchedSet());
        Set<String> ret =  matchedSet.getMatchedRule();
        ret.addAll(notSet.stream()
                .filter(s -> !matchedSet.matchedNotRule.contains(s))
                .collect(Collectors.toSet()));
        return ret;

    }

    @Override
    public MatchOperation supportedOperation() {
        return StringContains.getInstance();
    }

    @Override
    public void putRule(RuleLeaf<?, String> rule) {
        String exp = rule.expected();
        //TODO:Init 完成节点做一次reindex
        putAndReIndex(exp,rule.getIdentify(),rule.isNot());
    }

    public static BatchMatcher<String> newInstance() {
        BatchStrContains strContains = new BatchStrContains();
        return strContains;
    }

    @Override
    public String getIdentify() {
        return (String) supportedOperation().getIdentify();
    }
}
