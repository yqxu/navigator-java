package com.pingpongx.smb.rule.routers.operatiors;

import com.pingpongx.smb.export.RuleConstant;
import com.pingpongx.smb.export.module.MatchOperation;


public class StrNotBlank implements MatchOperation<String> {

    String obj;
    String attr;
    public static StrNotBlank getInstance(){
        return new StrNotBlank();
    }
    public static MatchOperation getInstance(String obj,String attr){
        return getInstance().attr(attr).obj(obj);
    }

    @Override
    public String getIdentify() {
        return RuleConstant.Operations.StrNotBlank.getSimpleName();
    }

    @Override
    public int sortBy() {
        return 0;
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
        return attr;
    }

    @Override
    public MatchOperation attr(String attr) {
        this.attr = attr;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StrNotBlank strEquals = (StrNotBlank) o;

        if (obj != null ? !obj.equals(strEquals.obj) : strEquals.obj != null) return false;
        return attr != null ? attr.equals(strEquals.attr) : strEquals.attr == null;
    }

    @Override
    public int hashCode() {
        int result = obj != null ? obj.hashCode() : 0;
        result = 31 * result + (attr != null ? attr.hashCode() : 0);
        return result;
    }
}
