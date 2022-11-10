package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch.BatchMatcher;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch.BatchMatcherFactory;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.moudle.Node;
import com.pingpongx.smb.warning.biz.moudle.Trie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class RuleTrie {
    Trie<String, Map<String,RuleHandler>> ruleTrie = new Trie<>();
    Trie<String, Map<String,RuleHandler>> notTrie = new Trie<>();


    @Autowired
    BatchMatcherMapper matcherMapper;

    @Autowired
    DataAttrMapper attrMapper ;


    Rule newAnd(Rule rule,Rule rule2){
        return RuleAnd.newAnd(rule).and(rule2);
    }

    Rule newOr(Rule rule,Rule rule2){
        return RuleOr.newOr(rule).or(rule2);
    }

    public RuleTrie put(Rule rule,RuleHandler handler){
        RuleOr or = rule.expansion();
        putOr(or,handler);
        return this;
    }

    public MatchResult match(Object data){
        String type = data.getClass().getSimpleName();
        Set<String> attrs = attrMapper.attrsOf(type);
        Set<String> matchedRulesRepeat = new HashSet<>();
        List<String> matchedRules = new ArrayList<>();
        Set<String> exceptRulesRepeat = new HashSet<>();
        List<String> exceptRules = new ArrayList<>();
        MatchResult result = new MatchResult();
        if (attrs == null){
            result.setMatchedData(new HashMap<>());
            return result;
        }
        attrs.stream().forEach(attr->{
            TreeMap<String,BatchMatcher> sortedMatcher = matcherMapper.routeMatchers(data,attr);
            Iterator<BatchMatcher> it = sortedMatcher.values().iterator();
            try{
                while(it.hasNext()){
                    Field field = data.getClass().getDeclaredField(attr);
                    field.setAccessible(true);
                    Object attrVal = ReflectionUtils.getField(field,data);
                    BatchMatcher matcher = it.next();
                    boolean logicOfNot = matcher.supportedOperation().logicOfNot();
                    Set<String> matchedRule = matcher.batchMatch(attrVal);
                    if (logicOfNot){
                        matchedRule.stream().forEach(r->{
                            if (!exceptRulesRepeat.contains(r)){
                                exceptRules.add(r);
                            }
                        });
                    }else {
                        matchedRule.stream().forEach(r->{
                            if (!matchedRulesRepeat.contains(r)){
                                matchedRules.add(r);
                            }
                        });
                    }
                }
            }catch (Exception e){
                log.error("获取字段失败:"+attr,e);
            }
        });
        IdentityPath<String> path = IdentityPath.of(matchedRules.stream().collect(Collectors.toList()));
        //TODO: 可以优化成向父追溯，为id 建立node的倒排索引，复杂度可以优化为On n=len（path）目前为O（n+1）*n/2 近似n方 n同为匹配条件数
        Map<String,Map<String,RuleHandler>> matched = ruleTrie.bfsGet(path);
        result.setMatchedData(matched);
        return result;
    }


    private RuleTrie putOnly(Rule rule,RuleHandler handler){
        if (rule instanceof RuleAnd){
            putAnd((RuleAnd) rule,handler);
        } else if (rule instanceof RuleOr) {
            putOr((RuleOr) rule,handler);
        }else{
            RuleAnd and = (RuleAnd) RuleAnd.newAnd(rule);
            putAnd(and,handler);
        }
        return this;
    }

    private RuleTrie putOr(RuleOr or, RuleHandler handler){
        or.orRuleSet.stream().forEach(r->putOnly(r,handler));
        return this;
    }

    private RuleTrie putAnd(RuleAnd and,RuleHandler handler){
        List<String> ids = and.andRuleList.stream().map(r->((RuleLeaf)r).getIdentify()).collect(Collectors.toList());
        IdentityPath<String> path = IdentityPath.of(ids);
        Node<String,Map<String,RuleHandler>> node = ruleTrie.getOrCreate(path);
        if (node.isNew()){
            node.setData(new ConcurrentHashMap<>());
        }
        handler.setPath(node.getPath());
        node.getData().put((String) handler.getIdentify(),handler);

        and.andRuleList.stream()
                .filter(r->r instanceof RuleLeaf)
                .forEach(r->{
                    //TODO NOT logic
                    String dpObj = ((RuleLeaf<?, ?>) r).dependsObject().getSimpleName();
                    String dpAttr = ((RuleLeaf<?, ?>) r).dependsAttr();
                    String opIdentity = (String) ((RuleLeaf<?, ?>) r).operatorType().getIdentify();
                    TreeMap<String,BatchMatcher>  matchers = matcherMapper.matchers(dpObj,dpAttr);
                    BatchMatcher matcher = matchers.get(opIdentity);
                    if (matcher==null){
                        matcher = BatchMatcherFactory.newBatchMatcher(opIdentity);
                        matcherMapper.put(dpObj,dpAttr,matcher);
                    }
                    matcher.putRule((RuleLeaf<?, ?>) r);
                });

        return this;
    }

}
