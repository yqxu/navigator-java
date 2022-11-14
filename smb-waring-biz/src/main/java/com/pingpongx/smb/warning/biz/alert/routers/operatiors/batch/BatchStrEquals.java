package com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StrEquals;
import com.pingpongx.smb.warning.biz.rules.RuleLeaf;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class BatchStrEquals implements BatchMatcher<String>{

    Map<String,MatchedSet> ruleMap = new ConcurrentHashMap<>();

    Set<String> notSet = new HashSet<>();

    public void putOnly(String str,String ruleIdentify,boolean isNot){
        if (isNot){
            notSet.add(ruleIdentify);
        }
        MatchedSet ret = ruleMap.get(str);
        if (ret == null){
            ret = new MatchedSet();
            ruleMap.put(str,ret);
        }
        if (isNot){
            ret.matchedNotRule.add(ruleIdentify);
        }else{
            ret.matchedRule.add(ruleIdentify);
        }
    }

    /****
     * 批量匹配字符串 为StringContains提供批量O（n）支撑
     * @param input 对象的待匹配属性，因为操作符的限制只能是string
     * @return 用了contains 规则的rule 的 identify 集合
     */
    public Set<String> batchMatch(String input){
        MatchedSet matchedSet = ruleMap.get(input);
        Set<String> ret = matchedSet.getMatchedRule();
        ret.addAll(notSet.stream().filter(s->matchedSet.getMatchedNotRule().contains(s)).collect(Collectors.toSet()));
        return ret;
    }

    @Override
    public MatchOperation supportedOperation() {
        return StrEquals.getInstance();
    }

    @Override
    public void putRule(RuleLeaf<?,String> rule) {
        String exp = rule.expected();
        putOnly(exp,rule.getIdentify(),rule.isNot());
    }
    @Override
    public String getIdentify() {
        return (String) supportedOperation().getIdentify();
    }

    public static BatchMatcher<String> newInstance() {
        BatchStrEquals strContains = new BatchStrEquals();
        return strContains;
    }
}
