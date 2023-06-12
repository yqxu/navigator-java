package com.pingpongx.smb.warning.biz.alert.threshold;

import com.alibaba.fastjson.JSON;
import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.rule.handler.AbstractRuleHandler;
import com.pingpongx.smb.warning.biz.alert.event.ToExecute;
import com.pingpongx.smb.warning.biz.alert.event.ToInhibition;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.List;

@Slf4j
public class SimpleCounterThresholdInhibition<T extends ThirdPartAlert> extends AbstractRuleHandler<T> implements Inhibition<T>{
    long threshold;
    ApplicationContext applicationContext;

    public static String IDENTIFY(){
        return "Inhibition";
    }
//    public InhibitionResultEnum needInhibition(T date,IdentityPath<String> path,MatchResult matchContext){
//        Map<String, RuleHandler> context = matchContext.getMatchedData().get(path);
//        if (context == null){
//            log.error("matched but no context found.Method:needInhibition,Class:"+this.getClass().getName()+"\npath:"+path.toString());
//            return InhibitionResultEnum.MatchedAndNeedThrow;
//        }
//        CountContext handler = (CountContext) context.get(CountContext.IDENTIFY());
//        Counter counter = handler.getCounter(date);
//        if (counter.sum()>=threshold){
//            return InhibitionResultEnum.MatchedAndNeedThrow;
//        }
//        return InhibitionResultEnum.MatchedAndNeedInhibition;
//    }

    private Integer getCount(PipelineContext context){
        String countStr = context.getParams().get("count");
        if (countStr == null){
            return null;
        }
        return Integer.valueOf(countStr);
    }

    public InhibitionResultEnum needInhibition(T date, PipelineContext context){
        if (getCount(context) == null){
            log.error("matched but no context found.Method:needInhibition,Class:"+this.getClass().getName()+"\nContext:"+ JSON.toJSONString(context));
            return InhibitionResultEnum.MatchedAndNeedThrow;
        }
        if (getCount(context)>=threshold){
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
        return IDENTIFY()+"|"+scene();
    }

//    @Override
//    public void handleMatchedData(T data,IdentityPath<String> path, MatchResult matchContext) {
//        InhibitionResultEnum inhibitionResult = needInhibition(data,path,matchContext);
//        if (InhibitionResultEnum.MatchedAndNeedInhibition.equals(inhibitionResult)){
//            ToInhibition toInhibition = new ToInhibition(applicationContext,data);
//            applicationContext.publishEvent(toInhibition);
//            return;
//        }
//        if (InhibitionResultEnum.MatchedAndNeedThrow.equals(inhibitionResult)){
//            log.error("告警被抑制后依然抛出.超出阈值\n"+ data.throwContent());
//            ToExecute toExecute = new ToExecute(applicationContext,data);
//            applicationContext.publishEvent(toExecute);
//            return;
//        }
//        log.error("matched inhibition rule . but no Inhibition strategy found."+this.getClass().getName());
//    }


    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public List<String> tags() {
        return null;
    }

    @Override
    public List<String> chainsOf() {
        return null;
    }

    @Override
    public void doAction(T data, PipelineContext context) {
        InhibitionResultEnum inhibitionResult = needInhibition(data,context);
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
}
