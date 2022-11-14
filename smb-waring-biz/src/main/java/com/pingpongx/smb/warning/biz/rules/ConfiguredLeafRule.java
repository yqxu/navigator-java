package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.OperationFactory;
import com.pingpongx.smb.warning.biz.rules.scene.configure.LeafRuleConf;

public class ConfiguredLeafRule extends RuleLeaf<ThirdPartAlert ,String> {

    String type;
    String attr;
    MatchOperation operation;
    String expected;
    boolean not;

    public static ConfiguredLeafRule resumeByConf(LeafRuleConf conf){
        ConfiguredLeafRule rule = new ConfiguredLeafRule();
        rule.attr = conf.getAttr();
        rule.type = conf.getType();
        rule.expected = conf.getExpected();
        rule.operation = OperationFactory.getInstance(conf.getOperation());
        rule.not = conf.isNot();
        return rule;
    }

    @Override
    public Class dependsObject() {
        if (MerchantAlert.class.getName().equals(type)){
            return MerchantAlert.class;
        }else {
            return SlsAlert.class;
        }
    }

    @Override
    public String dependsAttr() {
        return attr;
    }

    @Override
    public MatchOperation operatorType() {
        return operation;
    }

    @Override
    public String expected() {
        return expected;
    }

    @Override
    public boolean isNot() {
        return not;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public MatchOperation getOperation() {
        return operation;
    }

    public void setOperation(MatchOperation operation) {
        this.operation = operation;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }
}
