package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.Identified;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;
import lombok.Data;

import java.util.PriorityQueue;

public abstract class RuleLeaf<D,T> implements Rule<D,T>, Identified<String> {
    public abstract Class<D> dependsObject();

    public abstract String dependsAttr();


    public abstract MatchOperation operatorType();

    public abstract T excepted();

    public int compareTo(RuleLeaf<D, T> o) {
        return this.operatorType().sortBy()-o.operatorType().sortBy();
    }

    public String getIdentify(){
        return dependsObject().getSimpleName()+"."+dependsAttr()+"->"+operatorType().getIdentify()+":"+excepted();
    }
}
