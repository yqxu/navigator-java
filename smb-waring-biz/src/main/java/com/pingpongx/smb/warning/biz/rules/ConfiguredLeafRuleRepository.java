package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.rule.routers.operatiors.OperationFactory;
import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.rules.scene.configure.LeafRuleConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfiguredLeafRuleRepository {

    @Autowired
    Engine engine;

    public  ConfiguredLeafRule resumeByConf(LeafRuleConf conf){
        ConfiguredLeafRule rule = new ConfiguredLeafRule();
        rule.attr = conf.getAttr();
        rule.expected = conf.getExpected();
        rule.setType(engine.metadataCenter.get(conf.getType()));
        rule.operation = OperationFactory.getInstance(conf.getOperation(),rule.attr,rule.type);
        rule.not = conf.isNot();
        return rule;
    }

}
