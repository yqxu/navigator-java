package com.pingpongx.smb.warning.biz.alert.counter;

import java.util.concurrent.atomic.LongAdder;

public class CounterNode {
    public LongAdder getCount() {
        return count;
    }

    public long getTerm() {
        return term;
    }
    int index;
    LongAdder count = new LongAdder();
    long term = 0L;

    public CounterNode increment(long term){
        if (term != this.term){
            count.reset();
            this.term = term;
        }
        count.increment();
        return this;
    }

    public int getIndex() {
        return index;
    }

    public static CounterNode getInstance(int index){
        CounterNode ret = new CounterNode();
        ret.index = index;
        return ret;
    }
}
