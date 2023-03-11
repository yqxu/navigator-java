package com.pingpongx.smb.export.module.operation;


import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.export.module.persistance.And;
import com.pingpongx.smb.export.module.persistance.RuleDto;

import java.util.PriorityQueue;
import java.util.stream.Collectors;


public class RuleAnd implements Rule {
    PriorityQueue<Rule> andRuleList = new PriorityQueue<>();

    @Override
    public RuleOr expansion(){
        return andRuleList.stream().map(r->r.expansion()).reduce((or1,or2)->or1.and(or2)).get();
    }

    @Override
    public RuleDto toDto() {
        And and = new And();
        and.setAndRules(andRuleList.stream().map(rule -> rule.toDto()).collect(Collectors.toList()));
        return and;
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
    public String type() {
        if (andRuleList.size()<=0){
            return null;
        }
        return andRuleList.peek().type();
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

    @Override
    public int compareTo(Object o) {
        int one,other ;
        one = -1;
        if (o instanceof  RuleAnd){
            other = -1;
        }else if (o instanceof  RuleOr){
            other = -2;
        }else {
            other = ((RuleLeaf)o).operatorType().sortBy();
        }
        return one - other;
    }

    public PriorityQueue<Rule> getAndRuleList() {
        return andRuleList;
    }

    public void setAndRuleList(PriorityQueue<Rule> andRuleList) {
        this.andRuleList = andRuleList;
    }


}
