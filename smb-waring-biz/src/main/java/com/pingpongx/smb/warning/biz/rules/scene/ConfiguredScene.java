package com.pingpongx.smb.warning.biz.rules.scene;

import com.alibaba.fastjson.JSON;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.ThresholdAlertConf;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;
import com.pingpongx.smb.warning.biz.constant.Constant;
import com.pingpongx.smb.warning.biz.rules.*;
import com.pingpongx.smb.warning.biz.rules.scene.configure.And;
import com.pingpongx.smb.warning.biz.rules.scene.configure.LeafRuleConf;
import com.pingpongx.smb.warning.biz.rules.scene.configure.Or;
import com.pingpongx.smb.warning.biz.rules.scene.configure.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
@Component
public class ConfiguredScene {
    @Value("${configured.scene:}")
    private String configuredScenes;

    @Autowired
    RuleTrie ruleTrie;

    @Autowired
    InhibitionFactory inhibitionFactory;

    @PostConstruct
    void init(){
        loadConfigStr(configuredScenes);
    }

    public void loadConfigStr(String confStr){
        List<Scene> scenes = JSON.parseArray(confStr, Scene.class);
        if (scenes == null){
            return;
        }
        scenes.stream().forEach(scene -> {
            RuleOr rule = buildRule(scene.getRulesOf());
            CountContext countContext = new CountContext(scene.getCountWith());
            ruleTrie.put(rule, countContext);
            Inhibition<ThirdPartAlert> inhibition = inhibitionFactory.getInhibition(scene.getCountWith());
            ruleTrie.put(rule,inhibition);
        });
    }

    RuleOr buildRule(Or or){
        //TODO : DSL support.
        RuleOr ruleOr = new RuleOr();
        or.getOrRules().stream().map(and -> buildRule(and)).forEach(r->ruleOr.or(r));
        return ruleOr;
    }

    RuleAnd buildRule(And and){
        RuleAnd ruleAnd = new RuleAnd();
        and.getAndRules().stream().map(leafRuleConf -> ConfiguredLeafRule.resumeByConf(leafRuleConf)).forEach(r->ruleAnd.and(r));
        return ruleAnd;
    }

    public static void main(String args[]){
        Or or = new Or();
        And and = new And();
        LeafRuleConf rule = new LeafRuleConf();
        or.setOrRules(new ArrayList<>());
        or.getOrRules().add(and);
        and.setAndRules(new ArrayList<>());
        and.getAndRules().add(rule);
        rule.setAttr("content");
        rule.setType("SlsAlert");
        rule.setExcepted("Test");
        rule.setOperation(Constant.Operations.StrContains);
        Scene scene = new Scene();
        scene.setCountWith(new ThresholdAlertConf(5, TimeUnit.Minutes,10,10));
        scene.setRulesOf(or);

        System.out.println(JSON.toJSONString(scene));
    }
}
