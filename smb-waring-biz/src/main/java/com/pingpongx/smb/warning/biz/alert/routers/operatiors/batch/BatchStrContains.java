package com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.moudle.*;
import com.pingpongx.smb.warning.biz.rules.RuleLeaf;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BatchStrContains implements BatchMatcher<String>{
    ACTrie<Character,Set<String>> trie = new ACTrie<>();

    public void putAndReIndex(String str,String ruleIdentify){
        putOnly(str,ruleIdentify);
        trie.failBackRebuild();
    }

    public void putOnly(String str,String ruleIdentify){
        //规则入树
        IdentityPath<Character> path = IdentityPath.of(str.toCharArray());
        Node<Character,FSMNode<Character,Set<String>>> node = trie.getOrCreate(path,()->new FSMNode<>());

        if (node.getData().getData()==null){
            Set<String> ruleSet = new HashSet<>();
            ruleSet.add(ruleIdentify);
            FSMNode<Character,Set<String>> fsmNode = node.getData();
            fsmNode.setData(ruleSet);
        }else{
            node.getData().getData().add(ruleIdentify);
        }
    }

    /****
     * 批量匹配字符串 为StringContains提供批量O（n）支撑
     * @param input 对象的待匹配属性，因为操作符的限制只能是string
     * @return 用了contains 规则的rule 的 identify 集合
     */
    public Set<String> batchMatch(String input){
        IdentityPath<Character> path = IdentityPath.of(input.toCharArray());
        return trie.walk(path).stream().map(Node::getData).map(FSMNode::getData).filter(Objects::nonNull).reduce((set1,set2)->{
            set1.addAll(set2);
            return set1;
        }).orElse(new HashSet<>());
    }

    @Override
    public MatchOperation supportedOperation() {
        return StringContains.getInstance();
    }

    @Override
    public void putRule(RuleLeaf<?, String> rule) {
        String exp = rule.excepted();
        //TODO:Init 完成节点做一次reindex
        putAndReIndex(exp,rule.getIdentify());
    }

}
