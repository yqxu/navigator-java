package com.pingpongx.smb.warning.biz.alert.counter;

import com.pingpongx.smb.common.IdentityPath;
import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.rule.handler.AbstractRuleHandler;
import com.pingpongx.smb.warning.biz.alert.CountConf;
import com.pingpongx.smb.warning.biz.alert.global.GlobalCountContext;
import com.pingpongx.smb.warning.biz.alert.model.CountAble;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.rules.scene.configure.Scene;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CountContext extends AbstractRuleHandler<ThirdPartAlert> {
    private CountConf conf;
    public static String IDENTIFY(){
        return "Count";
    }
    public IdentityPath<String> buildCountPath(CountAble data){
        IdentityPath<String> fullPath = new IdentityPath<>();
        IdentityPath<String> countPath = data.countPath();
        fullPath.append(getIdentify()).appendAll(countPath);
        return fullPath;
    }
    public CountContext(Scene scene){
        super.sceneIdentity = scene.getIdentity();
        this.conf = scene.getCountWith();
    }

    public CountContext(String sceneId,CountConf conf){
        super.sceneIdentity = sceneId;
        this.conf = conf;
    }

    @Override
    public String getIdentify() {
        return IDENTIFY()+"|"+scene();
    }


    public Counter getCounter(ThirdPartAlert data){
        IdentityPath<String> fullPath = buildCountPath(data);
        return GlobalCountContext.getCounter(fullPath);
    }

    @Override
    public List<String> tags() {
        return Stream.of(sceneIdentity).collect(Collectors.toList());
    }

    @Override
    public List<String> chainsOf() {
        return Stream.of(scene()).collect(Collectors.toList());
    }

    @Override
    public void doAction(ThirdPartAlert data, PipelineContext context) {
        IdentityPath<String> fullPath = buildCountPath(data);
        Counter slidingCounter = GlobalCountContext.getCounter(fullPath);
        if (slidingCounter == null){
            slidingCounter = CounterFactory.getCounter(conf);
            GlobalCountContext.putCounter(fullPath,slidingCounter);
        }
        slidingCounter.increment();
        Map<String, String> params = context.getParams();
        params.put("count",slidingCounter.sum().toString());
    }
}
