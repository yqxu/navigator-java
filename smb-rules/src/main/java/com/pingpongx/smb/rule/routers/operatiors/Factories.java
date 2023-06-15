package com.pingpongx.smb.rule.routers.operatiors;

import com.pingpongx.smb.debug.*;
import com.pingpongx.smb.export.RuleConstant;
import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.*;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.rule.routers.operatiors.batch.*;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


public class Factories {

    public static class Operation{
        public static Map<String, Supplier<MatchOperation>> factories = new ConcurrentHashMap<>();
        static {
            factories.put(RuleConstant.Operations.StrEquals.getSimpleName(),()-> StrEquals.getInstance());
            factories.put(RuleConstant.Operations.StrContains.getSimpleName(),()->StringContains.getInstance());
            factories.put(RuleConstant.Operations.StrNotBlank.getSimpleName(),()->StrNotBlank.getInstance());
            factories.put(RuleConstant.Operations.NumEquals.getSimpleName(),()->NumEquals.getInstance());
            factories.put(RuleConstant.Operations.NumberRangeIn.getSimpleName(), ()->NumRangeIn.getInstance());
            factories.put(RuleConstant.Operations.InstanceOf.getSimpleName(), ()-> InstanceOf.getInstance());
        }
        public static MatchOperation getInstance(String name,String attr,String obj){
            try{
                return Optional.ofNullable(factories.get(name)).map(Supplier::get).map(op->op.obj(obj).attr(attr)).orElse(null);
            }catch (Exception e){
                System.out.println(name+","+obj+","+attr);
            }
            return null;
        }
    }

    public static class Matcher{
        public static Map<String, Supplier<BatchMatcher>> map = new ConcurrentHashMap<>();
        static {
            map.put(RuleConstant.Operations.StrEquals.getSimpleName(), BatchStrEquals::newInstance);
            map.put(RuleConstant.Operations.NumEquals.getSimpleName(),BatchNumEquals::newInstance);
            map.put(RuleConstant.Operations.StrNotBlank.getSimpleName(), BatchStrNotBlank::newInstance);
            map.put(RuleConstant.Operations.StrContains.getSimpleName(), BatchStrContains::newInstance);
            map.put(RuleConstant.Operations.NumberRangeIn.getSimpleName(), BatchNumRange::newInstance);
            map.put(RuleConstant.Operations.InstanceOf.getSimpleName(), BatchInstanceOf::newInstance);
        }

        public static BatchMatcher newBatchMatcher(String identify){
            return Optional.ofNullable(map.get(identify)).map(Supplier::get).orElse(null);
        }
    }

    public static class ConfiguredClass{
        public static Map<String, Supplier<ConfiguredRule>> map = new ConcurrentHashMap<>();
        static {
            map.put(RuleConstant.Operations.StrEquals.getSimpleName(), ConfiguredStrRule::new);
            map.put(RuleConstant.Operations.NumEquals.getSimpleName(), ConfiguredNumRule::new);
            map.put(RuleConstant.Operations.StrNotBlank.getSimpleName(), ConfiguredStrRule::new);
            map.put(RuleConstant.Operations.StrContains.getSimpleName(), ConfiguredStrRule::new);
            map.put(RuleConstant.Operations.NumberRangeIn.getSimpleName(), ConfiguredRangeRule::new);
        }

        public static ConfiguredRule instance(String identify){
            return Optional.ofNullable(map.get(identify)).map(Supplier::get).orElse(null);
        }
    }

    public static class DebugHandlers{
        public static void reg(String key,DebugHandler debugHandler){
            map.put(key,debugHandler);
        }
        public static Map<String, DebugHandler> map = new ConcurrentHashMap<>();
        static {
            reg(DebugHandlerTypes.NumberDelta.name() , new NumDeltaDebugHandler<>());
            reg(DebugHandlerTypes.ShowDeviation.name() , new ShowDeviationDebugHandler());
        }

        public static DebugHandler instance(String type, Engine engine, RuleLeaf ruleLeaf){
            return Optional.ofNullable(map.get(type)).map(builder -> builder.deepCopy().setRuleLeaf(ruleLeaf).setEngine(engine)).orElse(null);
        }
    }
}
