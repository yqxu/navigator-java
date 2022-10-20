package com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StrEquals;
import com.pingpongx.smb.warning.biz.rules.RuleLeaf;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BatchStrEquals implements BatchMatcher<String>{
    Map<String,Set<String>> ruleMap = new ConcurrentHashMap<>();

    public void putOnly(String str,String ruleIdentify){
        Set<String> ret = ruleMap.get(str);
        if (ret == null){
            ret = new HashSet<>();
            ret.add(ruleIdentify);
            ruleMap.put(str,ret);
            return;
        }
        ret.add(ruleIdentify);
    }

    /****
     * 批量匹配字符串 为StringContains提供批量O（n）支撑
     * @param input 对象的待匹配属性，因为操作符的限制只能是string
     * @return 用了contains 规则的rule 的 identify 集合
     */
    public Set<String> batchMatch(String input){
        return ruleMap.get(input);
    }

    @Override
    public MatchOperation supportedOperation() {
        return StrEquals.getInstance();
    }

    @Override
    public void putRule(RuleLeaf<?,String> rule) {
        String exp = rule.expected();
        Set<String> ruleSet = ruleMap.get(exp);
        if (ruleSet == null){
            ruleSet = new HashSet<>();
            ruleMap.put(exp,ruleSet);
        }
        ruleSet.add(rule.getIdentify());
    }
    @Override
    public String getIdentify() {
        return (String) supportedOperation().getIdentify();
    }

    @Override
    public BatchMatcher<String> newInstance() {
        BatchStrEquals strContains = new BatchStrEquals();
        return strContains;
    }
}
