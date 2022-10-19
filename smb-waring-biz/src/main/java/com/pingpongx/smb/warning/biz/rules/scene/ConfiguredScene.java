package com.pingpongx.smb.warning.biz.rules.scene;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.pingpongx.smb.warning.biz.alert.InhibitionFactory;
import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.alert.threshold.Inhibition;
import com.pingpongx.smb.warning.biz.rules.*;
import com.pingpongx.smb.warning.biz.rules.scene.configure.And;
import com.pingpongx.smb.warning.biz.rules.scene.configure.Or;
import com.pingpongx.smb.warning.biz.rules.scene.configure.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.List;

public class ConfiguredScene {
    @Value("${configured.scene:}")
    private String configuredScenes;

    @Autowired
    RuleTrie ruleTrie;

    @Autowired
    InhibitionFactory inhibitionFactory;

    @PostConstruct
    void init(){
        List<Scene> scenes = JSON.parseArray(configuredScenes, Scene.class);
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
        and.getAndRules().stream().forEach(r->ruleAnd.and(r));
        return ruleAnd;
    }
}
