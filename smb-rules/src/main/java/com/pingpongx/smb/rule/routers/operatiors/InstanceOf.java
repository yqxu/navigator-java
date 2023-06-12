package com.pingpongx.smb.rule.routers.operatiors;

import com.pingpongx.smb.export.RuleConstant;
import com.pingpongx.smb.export.module.MatchOperation;

import java.util.Objects;
import java.util.Optional;


public class InstanceOf implements MatchOperation<Object> {

    String obj;

    public static InstanceOf getInstance(){
        return new InstanceOf();
    }
    public static MatchOperation getInstance(String obj,String attr){
        return getInstance().attr(attr).obj(obj);
    }

    @Override
    public String getIdentify() {
        return RuleConstant.Operations.InstanceOf.getSimpleName();
    }

    @Override
    public int sortBy() {
        return 100000;
    }

    @Override
    public String obj() {
        return obj;
    }

    @Override
    public MatchOperation obj(String obj) {
        this.obj = obj;
        return this;
    }

    @Override
    public String attr() {
        return "Any";
    }

    @Override
    public MatchOperation attr(String attr) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(this,o);
    }

    @Override
    public int hashCode() {
        return obj.hashCode();
    }
}
