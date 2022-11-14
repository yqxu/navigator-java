package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.Identified;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;

import javax.validation.constraints.NotNull;

public abstract class RuleLeaf<D,T> implements Rule<D,T>, Identified<String> {
    public abstract Class<D> dependsObject();

    public abstract String dependsAttr();


    public abstract MatchOperation operatorType();

    public abstract T expected();

    public abstract boolean isNot();

    public int compareTo(RuleLeaf<D, T> o) {
        return this.operatorType().sortBy()-o.operatorType().sortBy();
    }

    public String getIdentify(){
        return dependsObject().getSimpleName()+"."+dependsAttr()+"->"+(isNot()?"!":"")+operatorType().getIdentify()+":"+ expected();
    }

    @Override
    public int compareTo(@NotNull Rule<D, T> o) {
        int one,other ;
        one = this.operatorType().sortBy();
        if (o instanceof  RuleAnd){
            other = -1;
        }else if (o instanceof  RuleOr){
            other = -2;
        }else {
            other =  this.operatorType().sortBy();
        }
        return one - other;
    }
}
