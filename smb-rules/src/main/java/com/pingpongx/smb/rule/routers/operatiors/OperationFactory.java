package com.pingpongx.smb.rule.routers.operatiors;

import com.pingpongx.smb.export.RuleConstant;
import com.pingpongx.smb.export.module.MatchOperation;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;


public class OperationFactory {
    public static Map<String, Supplier<MatchOperation>> factories = new ConcurrentHashMap<>();
    static {
        factories.put(RuleConstant.Operations.StrEquals,()-> StrEquals.getInstance());
        factories.put(RuleConstant.Operations.StrContains,()->StringContains.getInstance());
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
