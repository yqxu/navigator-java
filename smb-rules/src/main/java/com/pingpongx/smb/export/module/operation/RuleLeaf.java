package com.pingpongx.smb.export.module.operation;

import com.pingpongx.smb.export.module.Identified;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.export.module.persistance.LeafRuleConf;
import com.pingpongx.smb.export.module.persistance.RuleDto;

public abstract class RuleLeaf<T> implements Rule<T>, Identified<String> {
    public abstract String dependsObject();

    public abstract String dependsAttr();


    public abstract MatchOperation operatorType();

    public abstract T expected();

    public abstract boolean isNot();

    public String getIdentify(){
        return dependsObject()+"."+dependsAttr()+"->"+(isNot()?"!":"")+operatorType().getIdentify()+":"+ expected();
    }

    @Override
    public int compareTo(Rule<T> o) {
        if (o instanceof RuleAnd){
            return  1;
        }else if (o instanceof RuleOr){
            return  1;
        }
        int ret = this.operatorType().compareTo(((RuleLeaf)o).operatorType());
        if (ret == 0){
            //使用了tree set 结构，防止被去重,允许乱序
            return this.expected().equals(((RuleLeaf<Object>) o).expected())?0:-1;
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
        return this.getIdentify().equals(((RuleLeaf< ?>) obj).getIdentify());
    }

    public RuleDto toDto(){
        LeafRuleConf leafRule = new LeafRuleConf();
        leafRule.setOperation(this.operatorType().getIdentify().toString());
        leafRule.setExpected(this.expected().toString());
        leafRule.setAttr(this.dependsAttr());
        leafRule.setNot(this.isNot());
        leafRule.setType(this.type());
        return leafRule;
    }

}
