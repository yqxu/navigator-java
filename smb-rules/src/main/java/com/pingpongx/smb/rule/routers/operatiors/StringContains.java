package com.pingpongx.smb.rule.routers.operatiors;

import com.pingpongx.smb.export.RuleConstant;
import com.pingpongx.smb.export.module.MatchOperation;


public class StringContains implements MatchOperation<String> {
    public static StringContains getInstance(){
        return new StringContains();
    }

    public static MatchOperation getInstance(String obj,String attr){
        return getInstance().attr(attr).obj(obj);
    }

    String obj;
    String attr;

    @Override
    public String getIdentify() {
        return RuleConstant.Operations.StrContains.getSimpleName();
    }

    @Override
    public int sortBy() {
        return 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringContains that = (StringContains) o;

        if (obj != null ? !obj.equals(that.obj) : that.obj != null) return false;
        return attr != null ? attr.equals(that.attr) : that.attr == null;
    }

    @Override
    public int hashCode() {
        int result = obj != null ? obj.hashCode() : 0;
        result = 31 * result + (attr != null ? attr.hashCode() : 0);
        return result;
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
}
