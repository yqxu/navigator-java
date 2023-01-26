package com.pingpongx.smb.rule.routers.operatiors;

import com.pingpongx.smb.export.RuleConstant;
import com.pingpongx.smb.export.module.MatchOperation;


public class NumLessThen implements MatchOperation<Number> {
    public static NumLessThen getInstance(){
        return new NumLessThen();
    }
    public static MatchOperation getInstance(String obj,String attr){
        return getInstance().attr(attr).obj(obj);
    }
    String attr;
    String obj;

    @Override
    public String getIdentify() {
        return RuleConstant.Operations.NumBiggerThen;
    }

    @Override
    public int sortBy() {
        return 100;
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

        NumLessThen that = (NumLessThen) o;

        if (attr != null ? !attr.equals(that.attr) : that.attr != null) return false;
        return obj != null ? obj.equals(that.obj) : that.obj == null;
    }

    @Override
    public int hashCode() {
        int result = attr != null ? attr.hashCode() : 0;
        result = 31 * result + (obj != null ? obj.hashCode() : 0);
        return result;
    }
}
