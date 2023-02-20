package com.pingpongx.smb.rule.routers.operatiors;

import com.pingpongx.smb.export.RuleConstant;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.rule.routers.operatiors.batch.BatchMatcher;
import com.pingpongx.smb.rule.routers.operatiors.batch.BatchNumRange;
import com.pingpongx.smb.rule.routers.operatiors.batch.BatchStrContains;
import com.pingpongx.smb.rule.routers.operatiors.batch.BatchStrEquals;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


public class Factories {
    public static class Operation{
        public static Map<String, Supplier<MatchOperation>> factories = new ConcurrentHashMap<>();
        static {
            factories.put(RuleConstant.Operations.StrEquals,()-> StrEquals.getInstance());
            factories.put(RuleConstant.Operations.StrContains,()->StringContains.getInstance());
            factories.put(RuleConstant.Operations.NumEquals,()->NumEquals.getInstance());
            factories.put(RuleConstant.Operations.NumBiggerThen, ()->NumBiggerThen.getInstance());
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
            map.put(RuleConstant.Operations.StrEquals, BatchStrEquals::newInstance);
            map.put(RuleConstant.Operations.NumEquals,BatchStrEquals::newInstance);
            map.put(RuleConstant.Operations.StrContains, BatchStrContains::newInstance);
            map.put(RuleConstant.Operations.NumBiggerThen, BatchNumRange::newInstance);
        }

        public static BatchMatcher newBatchMatcher(String identify){
            return Optional.ofNullable(map.get(identify)).map(Supplier::get).orElse(null);
        }
    }


}
