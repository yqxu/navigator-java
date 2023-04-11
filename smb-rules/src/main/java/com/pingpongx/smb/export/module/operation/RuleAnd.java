package com.pingpongx.smb.export.module.operation;


import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.export.module.persistance.And;
import com.pingpongx.smb.export.module.persistance.RuleDto;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class RuleAnd implements Rule {
    TreeSet<Rule> andRuleList = new TreeSet<>();

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
        return andRuleList.first().type();
    }

    @Override
    public Rule and(Rule rule){
        if (rule instanceof RuleAnd){
            andRuleList.addAll(((RuleAnd) rule).getAndRuleList());
            return this;
        }else if (rule instanceof RuleOr){
            return ((RuleOr)rule).and(this);
        }else{
            andRuleList.add(rule);
            return this;
        }
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof  RuleAnd){
            return 0;
        }else if (o instanceof  RuleOr){
            return 1;
        }else {
            return -1;
        }
    }

    public TreeSet<Rule> getAndRuleList() {
        return andRuleList;
    }

    public void setAndRuleList(TreeSet<Rule> andRuleList) {
        this.andRuleList = andRuleList;
    }


}
