package com.pingpongx.smb.warning.biz.alert.threshold;

import com.pingpongx.smb.warning.biz.alert.counter.CountContext;
import com.pingpongx.smb.warning.biz.alert.counter.Counter;
import com.pingpongx.smb.warning.biz.alert.counter.SlidingCounter;
import com.pingpongx.smb.warning.biz.alert.event.ToExecute;
import com.pingpongx.smb.warning.biz.alert.event.ToInhibition;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.rules.AbstractRuleHandler;
import com.pingpongx.smb.warning.biz.rules.MatchResult;
import com.pingpongx.smb.warning.biz.rules.RuleHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.Map;
@Slf4j
public class SimpleCounterThresholdInhibition<T extends ThirdPartAlert> extends AbstractRuleHandler<T> implements Inhibition<T>{
    long threshold;
    ApplicationContext applicationContext;

    public static String IDENTIFY(){
        return "Inhibition";
    }
    public InhibitionResultEnum needInhibition(T date,MatchResult matchContext){
        Map<String, RuleHandler> context = matchContext.getMatchedData().get(getPath().toString());
        if (context == null){
            log.error("matched but no context found.Method:needInhibition,Class:"+this.getClass().getName());
        }
        if (context == null){
            log.error("matched but no counter found.Method:needInhibition,Class:"+this.getClass().getName());
        }
        CountContext handler = (CountContext) context.get(CountContext.IDENTIFY());
        Counter counter = handler.getCounter(date);
        if (counter.sum()>=threshold){
            return InhibitionResultEnum.MatchedAndNeedThrow;
        }
        return InhibitionResultEnum.MatchedAndNeedInhibition;
    }


    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }

    @Override
    public String getIdentify() {
        return IDENTIFY();
    }

    @Override
    public void handleMatchedData(T data, MatchResult matchContext) {
        InhibitionResultEnum inhibitionResult = needInhibition(data,matchContext);

        if (InhibitionResultEnum.MatchedAndNeedInhibition.equals(inhibitionResult)){
            ToInhibition toInhibition = new ToInhibition(applicationContext,data);
            applicationContext.publishEvent(toInhibition);
            return;
        }
        if (InhibitionResultEnum.MatchedAndNeedThrow.equals(inhibitionResult)){
            log.error("告警被抑制后依然抛出.超出阈值\n"+ data.throwContent());
            ToExecute toExecute = new ToExecute(applicationContext,data);
            applicationContext.publishEvent(toExecute);
            return;
        }
        log.error("matched inhibition rule . but no Inhibition strategy found."+this.getClass().getName());
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
