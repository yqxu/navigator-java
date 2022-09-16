package com.pingpongx.smb.warning.biz.alert.threshold;

import com.pingpongx.smb.warning.biz.alert.counter.SlidingCounter;
import com.pingpongx.smb.warning.biz.rules.Rule;

public class ThresholdInhibition<T> implements Inhibition<T>{
    //chain needed?
    Rule<T> matcher;
    SlidingCounter counter;
    long threshold;

    public boolean needInhibition(T date){
        if (!matcher.contentMatch(date)){
            return false;
        }
        counter.increment();
        if (counter.sum()>=threshold){
            return false;
        }
        return true;
    }

    public Rule<T> getMatcher() {
        return matcher;
    }

    public void setMatcher(Rule<T> matcher) {
        this.matcher = matcher;
    }

    public SlidingCounter getCounter() {
        return counter;
    }

    public void setCounter(SlidingCounter counter) {
        this.counter = counter;
    }

    public long getThreshold() {
        return threshold;
    }

    public void setThreshold(long threshold) {
        this.threshold = threshold;
    }
}
