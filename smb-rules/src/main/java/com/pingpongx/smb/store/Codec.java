package com.pingpongx.smb.store;

import com.pingpongx.smb.export.module.ConfiguredLeafRule;
import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.export.module.operation.RuleAnd;
import com.pingpongx.smb.export.module.operation.RuleOr;
import com.pingpongx.smb.export.module.persistance.And;
import com.pingpongx.smb.export.module.persistance.LeafRuleConf;
import com.pingpongx.smb.export.module.persistance.Or;
import com.pingpongx.smb.export.module.persistance.RuleDto;
import com.pingpongx.smb.rule.routers.operatiors.Factories;

public class Codec {
    public static RuleOr buildRule(Or or){
        //TODO : DSL support.
        RuleOr ruleOr = new RuleOr();
        or.getOrRules().stream().map(and -> buildRule(and)).forEach(r->ruleOr.or(r));
        return ruleOr;
    }

    public static Rule buildRule(RuleDto ruleDto){
        if (ruleDto instanceof And){
            return buildRule((And)ruleDto);
        }
        if (ruleDto instanceof Or){
            return buildRule((Or)ruleDto);
        }
        return toLeafRule((LeafRuleConf) ruleDto);
    }

    public static RuleAnd buildRule(And and){
        RuleAnd ruleAnd = new RuleAnd();
        and.getAndRules().stream()
                .map(r -> buildRule(r))
                .forEach(r->ruleAnd.or(r));
        return ruleAnd;
    }

    public static ConfiguredLeafRule toLeafRule(LeafRuleConf conf){
        ConfiguredLeafRule rule = new ConfiguredLeafRule();
        rule.setAttr(conf.getAttr());
        rule.setExpected(conf.getExpected());
        rule.setType(conf.getType());
        rule.setOperation(Factories.Operation.getInstance(conf.getOperation(),rule.getAttr(),rule.getType()));
        rule.setNot(conf.isNot());
        return rule;
    }
}
