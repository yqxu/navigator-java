package com.pingpongx.smb.export.globle;

import com.pingpongx.smb.export.metadata.ToMatchTypeCenter;
import com.pingpongx.smb.export.module.MatchResult;
import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.export.module.operation.RuleAnd;
import com.pingpongx.smb.export.module.operation.RuleOr;
import com.pingpongx.smb.export.spi.AttrExtractor;
import com.pingpongx.smb.export.spi.RuleHandler;
import com.pingpongx.smb.rule.extractors.JavaAttrExtractor;
import com.pingpongx.smb.export.module.RuleTrie;
import com.pingpongx.smb.store.BatchMatcherMapper;
import com.pingpongx.smb.store.DataAttrMapper;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Engine {
    public static Engine newInstance(){
        return new Engine();
    }
    public BatchMatcherMapper batchMatcherMapper = new BatchMatcherMapper(this);

    public DataAttrMapper dataAttrMapper = new DataAttrMapper();
    public AttrExtractor extractor = new JavaAttrExtractor();

    public ToMatchTypeCenter metadataCenter = new ToMatchTypeCenter();

    public Map<Class,RuleTrie> ruleTries = new ConcurrentHashMap<>();


    public static Rule newAnd(Rule rule, Rule rule2){
        return RuleAnd.newAnd(rule).and(rule2);
    }

    public static Rule newOr(Rule rule,Rule rule2){
        return RuleOr.newOr(rule).or(rule2);
    }

    public void put(Rule rule, RuleHandler handler){
        RuleOr or = rule.expansion();
        RuleTrie ruleTrie = ruleTries.get(rule.type());
        if (ruleTrie == null){
            ruleTrie = new RuleTrie(this);
            ruleTries.put(rule.type(),ruleTrie);
        }
        ruleTrie.putOr(or,handler);
    }


    public MatchResult match(Class clazz,Object data){
        RuleTrie ruleTrie = ruleTries.get(clazz);
        if (ruleTrie == null){
            MatchResult ret = new MatchResult();
            ret.setMatchedData(new HashSet<>());
            return ret;
        }
        return ruleTrie.match(data);
    }
}
