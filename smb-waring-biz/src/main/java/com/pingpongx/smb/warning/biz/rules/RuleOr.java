package com.pingpongx.smb.warning.biz.rules;

import com.baomidou.mybatisplus.extension.api.R;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleOr implements Rule{
    Set<Rule> orRuleSet = new HashSet<>();

    @Override
    public RuleOr expansion(){
        orRuleSet = orRuleSet.stream().map(r->r.expansion()).flatMap(or->or.orRuleSet.stream()).collect(Collectors.toSet());
        return this;
    }

    @Override
    public RuleOr or(Rule rule){
        if (rule instanceof RuleOr){
            orRuleSet.addAll(((RuleOr) rule).orRuleSet);
            return this;
        }
        orRuleSet.add(rule);
        return this;
    }

    @Override
    public RuleOr and(Rule rule){
        if (rule instanceof RuleOr){
            RuleOr ret = new RuleOr();
            this.orRuleSet.forEach(set1Rule->{
                ((RuleOr) rule).orRuleSet.forEach(set2Rule->{
                    Rule and = RuleAnd.newAnd(set1Rule).and(set2Rule);
                    ret.or(and);
                });
            });
            return ret;
        }
        Set<Rule> or = orRuleSet.stream().map(t->RuleAnd.newAnd(t).and(rule)).collect(Collectors.toSet());
        RuleOr newOr = new RuleOr();
        newOr.orRuleSet = or;
        return newOr;
    }

    public static RuleOr newOr(Rule rule){
        if (rule instanceof RuleOr){
            return (RuleOr) rule;
        }else{
            RuleOr or = new RuleOr();
            or.or(rule);
            return or;
        }
    }

    @Override
    public int compareTo(Object o) {
        int one,other ;
        one = -2;
        if (o instanceof  RuleAnd){
            other = -1;
        }else if (o instanceof  RuleOr){
            other = -2;
        }else {
            other = ((RuleLeaf)o).operatorType().sortBy();
        }
        return one - other;
    }

}
