package com.pingpongx.smb.warning.biz.alert.threshold;

//public class BizExceptionThresholdInhibitionBack<T> implements Inhibition<T>{
//    //chain needed?
//    Rule<T> matcher;
//    SlidingCounter counter;
//    long threshold;
//
//    public InhibitionResultEnum needInhibition(T date, MatchResult matchContext){
//        if (!matcher.doMatch(date)){
//            return InhibitionResultEnum.UnMatched;
//        }
//        counter.increment();
//        if (counter.sum()>=threshold){
//            return InhibitionResultEnum.MatchedAndNeedThrow;
//        }
//        return InhibitionResultEnum.MatchedAndNeedInhibition;
//    }
//
//    public Rule<T> getMatcher() {
//        return matcher;
//    }
//
//    public void setMatcher(Rule<T> matcher) {
//        this.matcher = matcher;
//    }
//
//    public SlidingCounter getCounter() {
//        return counter;
//    }
//
//    public void setCounter(SlidingCounter counter) {
//        this.counter = counter;
//    }
//
//    public long getThreshold() {
//        return threshold;
//    }
//
//    public void setThreshold(long threshold) {
//        this.threshold = threshold;
//    }
//}
