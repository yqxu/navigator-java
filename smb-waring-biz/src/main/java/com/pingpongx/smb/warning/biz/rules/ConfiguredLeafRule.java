package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;

public class ConfiguredLeafRule extends RuleLeaf<ThirdPartAlert ,String> {

    String type;
    String attr;
    MatchOperation operation;
    String excepted;

    public static ConfiguredLeafRule resumeByConf(){
        //TODO:
        return null;
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
    public String excepted() {
        return excepted;
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

    public String getExcepted() {
        return excepted;
    }

    public void setExcepted(String excepted) {
        this.excepted = excepted;
    }
}
