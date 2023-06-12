package com.pingpongx.smb.export.module.operation;

import com.pingpongx.smb.debug.DebugHandler;
import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.*;
import com.pingpongx.smb.export.module.persistance.LeafRuleConf;
import com.pingpongx.smb.export.module.persistance.RuleDto;
import com.pingpongx.smb.rule.routers.operatiors.Factories;
import com.pingpongx.smb.rule.routers.operatiors.InstanceOf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RuleLeaf<T> implements Rule<T>, Identified<String> {
    public abstract String dependsObject();

    public abstract String dependsAttr();


    public abstract MatchOperation operatorType();

    public abstract T expected();

    public abstract boolean isNot();

    public abstract boolean needDebug();

    public abstract List<String> getDebugHandlerCodes();

    public abstract void setDebugHandlerCodes(List<String> debugHandlerCodes);

    public String getIdentify(){
        return dependsObject()+"."+dependsAttr()+"->"+(isNot()?"!":"")+operatorType().getIdentify()+":"+ expected();
    }

    @Override
    public int compareTo(Rule<T> o) {
        if (o instanceof RuleAnd){
            return  1;
        }else if (o instanceof RuleOr){
            return  1;
        }
        int ret = this.operatorType().compareTo(((RuleLeaf)o).operatorType());
        if (ret != 0 ){
            return ret;
        }
        ret = this.expected().toString().compareTo(((RuleLeaf<Object>) o).expected().toString());
        if (ret != 0 ){
            return ret;
        }
        ret = String.valueOf(this.isNot()).compareTo(String.valueOf(((RuleLeaf<Object>) o).isNot()));
        return ret;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RuleLeaf)){
            return false;
        }
        return this.getIdentify().equals(((RuleLeaf< ?>) obj).getIdentify());
    }

    public RuleDto toDto(){
        LeafRuleConf leafRule = new LeafRuleConf();
        leafRule.setOperation(this.operatorType().getIdentify().toString());
        leafRule.setExpected(this.expected().toString());
        leafRule.setAttr(this.dependsAttr());
        leafRule.setNot(this.isNot());
        leafRule.setType(this.type());
        leafRule.setDebugHandlerCodes(this.getDebugHandlerCodes());
        return leafRule;
    }
    public List<DebugHandler> buildDebugHandlers(Engine engine){
        if (!this.needDebug()){
            return new ArrayList<>();
        }
        return getDebugHandlerCodes().stream().map(handlerCode->Factories.DebugHandlers.instance(handlerCode,engine,this)).collect(Collectors.toList());
    }

    public ConfiguredRule debugReplaceRule(){
        ConfiguredRule configuredRule = new ConfiguredStrRule();
        configuredRule.setOperation(InstanceOf.getInstance(this.dependsObject(),this.dependsAttr()));
        configuredRule.setExpected(this.dependsObject());
        configuredRule.setNot(false);
        configuredRule.setType(this.dependsObject());
        configuredRule.setAttr(this.dependsAttr());
        return configuredRule;
    }
}
