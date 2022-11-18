package com.pingpongx.smb.warning.biz.alert.counter;

import com.pingpongx.smb.warning.biz.alert.CountConf;
import com.pingpongx.smb.warning.biz.alert.event.CountDone;
import com.pingpongx.smb.warning.biz.alert.global.GlobalCountContext;
import com.pingpongx.smb.warning.biz.alert.model.CountAble;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.rules.AbstractRuleHandler;
import com.pingpongx.smb.warning.biz.rules.MatchResult;
import com.pingpongx.smb.warning.biz.rules.PipelineContext;
import com.pingpongx.smb.warning.biz.rules.scene.configure.Scene;

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

    @Override
    public void handleMatchedData(ThirdPartAlert data, PipelineContext matchContext) {
        IdentityPath<String> fullPath = buildCountPath(data);
        Counter slidingCounter = GlobalCountContext.getCounter(fullPath);
        if (slidingCounter == null){
            slidingCounter = CounterFactory.getCounter(conf);
            GlobalCountContext.putCounter(fullPath,slidingCounter);
        }
        slidingCounter.increment();
        matchContext.setCount(slidingCounter.sum());
    }

    public Counter getCounter(ThirdPartAlert data){
        IdentityPath<String> fullPath = buildCountPath(data);
        return GlobalCountContext.getCounter(fullPath);
    }
}
