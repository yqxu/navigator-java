package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.rule.routers.operatiors.OperationFactory;
import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.rules.scene.configure.LeafRuleConf;

public class ConfiguredLeafRule extends RuleLeaf<ThirdPartAlert ,String> {
    Class type;
    String attr;
    MatchOperation operation;
    String expected;
    boolean not;

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

    public void setNot(boolean not) {
        this.not = not;
    }

    public void setType(Class type) {
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

    @Override
    public Class type() {
        return type;
    }
}
