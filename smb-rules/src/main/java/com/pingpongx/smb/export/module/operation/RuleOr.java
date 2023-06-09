package com.pingpongx.smb.export.module.operation;

import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.export.module.persistance.Or;
import com.pingpongx.smb.export.module.persistance.RuleDto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class RuleOr implements Rule {

    Set<Rule> orRuleSet = new HashSet<>();

    public static RuleOr newOr(Rule rule) {
        if (rule instanceof RuleOr) {
            return (RuleOr) rule;
        } else {
            RuleOr or = new RuleOr();
            or.or(rule);
            return or;
        }
    }

    @Override
    public RuleOr expansion() {
        orRuleSet = orRuleSet.stream().map(r -> r.expansion()).flatMap(or -> or.orRuleSet.stream()).collect(Collectors.toSet());
        return this;
    }

    @Override
    public RuleDto toDto() {
        Or or = new Or();
        or.setOrRules(orRuleSet.stream().map(rule -> rule.toDto()).collect(Collectors.toList()));
        return or;
    }

    @Override
    public RuleOr or(Rule rule) {
        if (rule instanceof RuleOr) {
            orRuleSet.addAll(((RuleOr) rule).orRuleSet);
            return this;
        }
        orRuleSet.add(rule);
        return this;
    }

    @Override
    public String type() {
        if (orRuleSet.size() <= 0) {
            return null;
        }
        return orRuleSet.iterator().next().type();
    }

    @Override
    public RuleOr and(Rule rule) {
        if (rule instanceof RuleOr) {
            RuleOr ret = new RuleOr();
            this.orRuleSet.forEach(set1Rule -> {
                ((RuleOr) rule).orRuleSet.forEach(set2Rule -> {
                    Rule and = RuleAnd.newAnd(set1Rule).and(set2Rule);
                    ret.or(and);
                });
            });
            return ret;
        }
        Set<Rule> or = orRuleSet.stream().map(t -> RuleAnd.newAnd(t).and(rule)).collect(Collectors.toSet());
        RuleOr newOr = new RuleOr();
        newOr.orRuleSet = or;
        return newOr;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof RuleAnd) {
            return -1;
        } else if (o instanceof RuleOr) {
            return 0;
        } else {
            return -1;
        }
    }

    public Set<Rule> getOrRuleSet() {
        return orRuleSet;
    }

    public void setOrRuleSet(Set<Rule> orRuleSet) {
        this.orRuleSet = orRuleSet;
    }
}
