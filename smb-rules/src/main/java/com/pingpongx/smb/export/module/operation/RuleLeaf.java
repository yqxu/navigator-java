package com.pingpongx.smb.export.module.operation;

import com.pingpongx.smb.export.module.Identified;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.export.module.Rule;

public abstract class RuleLeaf<D,T> implements Rule<D,T>, Identified<String> {
    public abstract Class<D> dependsObject();

    public abstract String dependsAttr();


    public abstract MatchOperation operatorType();

    public abstract T expected();

    public abstract boolean isNot();

    public String getIdentify(){
        return dependsObject().getSimpleName()+"."+dependsAttr()+"->"+(isNot()?"!":"")+operatorType().getIdentify()+":"+ expected();
    }

    @Override
    public int compareTo(Rule<D, T> o) {
        int one,other ;
        one = this.operatorType().sortBy();
        if (o instanceof RuleAnd){
            other = -1;
        }else if (o instanceof RuleOr){
            other = -2;
        }else {
            other =  ((RuleLeaf<D,T>)o).operatorType().sortBy();
        }
        int ret = one - other;
        if (ret == 0){
            ret = this.dependsAttr().compareTo(((RuleLeaf<D,T>)o).dependsAttr());
        }
        if (ret == 0){
            ret = this.getIdentify().compareTo(((RuleLeaf<D,T>)o).getIdentify());
        }
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
        return this.getIdentify().equals(((RuleLeaf<?, ?>) obj).getIdentify());
    }
}
