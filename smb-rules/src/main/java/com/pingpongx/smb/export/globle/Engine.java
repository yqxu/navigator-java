package com.pingpongx.smb.export.globle;

import com.pingpongx.smb.debug.DebugHandler;
import com.pingpongx.smb.export.metadata.ToMatchTypeCenter;
import com.pingpongx.smb.export.module.MatchResult;
import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.export.module.operation.RuleAnd;
import com.pingpongx.smb.export.module.operation.RuleOr;
import com.pingpongx.smb.export.spi.AttrExtractor;
import com.pingpongx.smb.export.spi.RuleHandler;
import com.pingpongx.smb.rule.extractors.JavaAttrExtractor;
import com.pingpongx.smb.export.module.RuleTrie;
import com.pingpongx.smb.rule.routers.operatiors.Factories;
import com.pingpongx.smb.store.BatchMatcherMapper;
import com.pingpongx.smb.store.DataAttrMapper;

public class Engine {
    public Engine(boolean b) {
        this.enableDebug = b;
    }

    public Engine() {
        this.enableDebug = false;
    }

    public static Engine newInstance(){
        return new Engine();
    }
    public static Engine newDebugAbleInstance(){
        return new Engine(true);
    }
    public BatchMatcherMapper batchMatcherMapper = new BatchMatcherMapper(this);

    public DataAttrMapper dataAttrMapper = new DataAttrMapper();
    public AttrExtractor extractor = new JavaAttrExtractor();

    public ToMatchTypeCenter metadataCenter = new ToMatchTypeCenter();

    public RuleTrie ruleTrie = new RuleTrie(this);

    public RuleTrie debugTrie = new RuleTrie(this);

    public final boolean enableDebug;

    Factories factories = new Factories();

    public static Rule newAnd(Rule rule, Rule rule2){
        return RuleAnd.newAnd(rule).and(rule2);
    }

    public static Rule newOr(Rule rule,Rule rule2){
        return RuleOr.newOr(rule).or(rule2);
    }

    public void put(Rule rule, RuleHandler handler){
        if (rule == null){
            throw new RuntimeException("rule can't be null.");
        }
        RuleOr or = rule.expansion();
        if (ruleTrie == null){
            ruleTrie = new RuleTrie(this);
        }
        ruleTrie.putOr(or,handler);
        if (enableDebug){
            debugTrie.deBugPutOr(or,this);
        }
    }

    public void putDebug(Rule rule){
        if (rule == null){
            throw new RuntimeException("rule can't be null.");
        }
        RuleOr or = rule.expansion();
        if (debugTrie == null){
            debugTrie = new RuleTrie(this);
        }
        debugTrie.debugPut(or,this);
    }

    public void putDebug(Rule rule, DebugHandler handler){
        if (rule == null){
            throw new RuntimeException("rule can't be null.");
        }
        RuleOr or = rule.expansion();
        if (debugTrie == null){
            debugTrie = new RuleTrie(this);
        }
        debugTrie.debugPut(or,handler);
    }

    public void remove(Rule rule, RuleHandler handler){
        if (rule == null){
            throw new RuntimeException("rule can't be null.");
        }
        RuleOr or = rule.expansion();
        if (ruleTrie == null){
            ruleTrie = new RuleTrie(this);
        }
        ruleTrie.putOr(or,handler);
    }

    /****
     * using com.pingpongx.smb.export.globle.Engine#match(java.lang.Object) instead.
     * @param clazz
     * @param data
     * @return
     */
    @Deprecated
    public MatchResult match(String clazz,Object data){
        return ruleTrie.match(data);
    }

    public MatchResult debug(Object data){
        return debugTrie.match(data);
    }

    public MatchResult match(Object data){
        return ruleTrie.match(data);
    }
}
