package com.pingpongx.smb.warning.biz.rules;

import lombok.Data;

import java.util.PriorityQueue;

@Data
public class RuleAnd implements Rule{
    PriorityQueue<Rule> andRuleList = new PriorityQueue<>();

    @Override
    public RuleOr expansion(){
        return andRuleList.stream().map(r->r.expansion()).reduce((or1,or2)->or1.and(or2)).get();
    }
    public static Rule newAnd(Rule rule){
        if (rule instanceof RuleAnd){
            return rule;
        }else if (rule instanceof RuleOr){
            return rule;
        }else{
            RuleAnd and = new RuleAnd();
            and.and(rule);
            return and;
        }
    }
    @Override
    public Rule and(Rule rule){
        if (rule instanceof RuleAnd){
            andRuleList.addAll(((RuleAnd) rule).getAndRuleList());
            return this;
        }else if (rule instanceof RuleOr){
            return ((RuleOr)rule).and(this);
        }else{
            andRuleList.offer(rule);
            return this;
        }
    }

}
