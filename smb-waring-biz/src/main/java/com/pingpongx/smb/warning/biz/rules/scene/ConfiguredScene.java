package com.pingpongx.smb.warning.biz.rules.scene;

import com.alibaba.fastjson2.JSON;
import com.pingpongx.smb.export.RuleConstant;
import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.Rule;
import com.pingpongx.smb.export.module.operation.RuleOr;
import com.pingpongx.smb.export.module.persistance.And;
import com.pingpongx.smb.export.module.persistance.LeafRuleConf;
import com.pingpongx.smb.export.module.persistance.Or;
import com.pingpongx.smb.export.module.persistance.RuleDto;
import com.pingpongx.smb.rule.routers.operatiors.Factories;
import com.pingpongx.smb.store.Codec;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.rules.scene.configure.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ConfiguredScene {
    @Autowired
    Engine engine;
    @Autowired
    InhibitionFactory inhibitionFactory;

    public static void main(String[] args) {
        Or or = new Or();
        And and = new And();
        LeafRuleConf rule = new LeafRuleConf();
        or.setOrRules(new ArrayList<>());
        or.getOrRules().add(and);
        and.setAndRules(new ArrayList<>());
        and.getAndRules().add(rule);
        rule.setAttr("content");
        rule.setType("SlsAlert");
        rule.setExpected("Test");
        rule.setOperation(RuleConstant.Operations.StrContains.getSimpleName());
        Scene scene = new Scene();
        scene.setCountWith(new ThresholdAlertConf(5, TimeUnit.Minutes, 10, 10));
        scene.setRulesOf(or);

        System.out.println(JSON.toJSONString(scene));
    }

    public void loadConfigStr(String confStr) {
        Scene scenet = new Scene();
        Or or = new Or();
        And and = new And();
        LeafRuleConf conf = new LeafRuleConf();
        conf.setType("type");
        conf.setNot(false);
        conf.setExpected("32423");
        conf.setOperation("NumberRangeIn");
        conf.setAttr("attr");
        and.setAndRules(Stream.of(conf).collect(Collectors.toList()));
        or.setOrRules(Stream.of(and).collect(Collectors.toList()));
        scenet.setRulesOf(or);
        String strTest = JSON.toJSONString(or);
        JSON.parseObject(strTest,RuleDto.class);

        List<Scene> scenes = JSON.parseArray(confStr, Scene.class);
        if (scenes == null) {
            return;
        }
        scenes.stream().forEach(scene -> {
            Rule rule = Codec.buildRule(scene.getRulesOf());
            CountContext countContext = new CountContext(scene);
            engine.put(rule, countContext);
            Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.newInhibition(scene.getIdentity(), scene.getCountWith());
            engine.put(rule, inhibition);
        });
    }
}
