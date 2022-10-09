package com.pingpongx.smb.warning.biz.alert.counter;

import com.pingpongx.smb.warning.biz.alert.CountConf;
import com.pingpongx.smb.warning.biz.alert.global.GlobalCountContext;
import com.pingpongx.smb.warning.biz.alert.model.CountAble;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.rules.AbstractRuleHandler;
import com.pingpongx.smb.warning.biz.rules.MatchResult;

public class CountContext extends AbstractRuleHandler<ThirdPartAlert> {
    private GlobalCountContext globalCountContext;

    private CountConf conf;

    public static String IDENTIFY(){
        return "Count";
    }
    public IdentityPath<String> buildCountPath(CountAble data){
        IdentityPath<String> fullPath = new IdentityPath<>();
        IdentityPath<String> countPath = data.countPath();
        fullPath.appendAll(getPath()).appendAll(countPath);
        return fullPath;
    }
    public CountContext(GlobalCountContext globalCountContext,CountConf conf){
        this.globalCountContext = globalCountContext;
        this.conf = conf;
    }

    @Override
    public String getIdentify() {
        return IDENTIFY();
    }

    @Override
    public void handleMatchedData(ThirdPartAlert data, MatchResult matchContext) {
        IdentityPath<String> fullPath = buildCountPath(data);
        Counter slidingCounter = globalCountContext.getCounter(fullPath);
        if (slidingCounter == null){
            slidingCounter = CounterFactory.getCounter(conf);
            globalCountContext.putCounter(fullPath,slidingCounter);
        }
        slidingCounter.increment();
    }

    public Counter getCounter(ThirdPartAlert data){
        IdentityPath<String> fullPath = buildCountPath(data);
        return globalCountContext.getCounter(fullPath);
    }
}
