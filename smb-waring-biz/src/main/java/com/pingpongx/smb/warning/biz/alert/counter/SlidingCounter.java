package com.pingpongx.smb.warning.biz.alert.counter;

import com.google.common.base.Preconditions;
import com.pingpongx.business.common.exception.BizException;
import com.pingpongx.business.common.exception.ErrorCode;
import com.pingpongx.smb.warning.biz.alert.AlertConf;
import com.pingpongx.smb.warning.biz.alert.CountConf;
import com.pingpongx.smb.warning.biz.alert.threshold.TimeUnit;

import java.util.ArrayList;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

public class SlidingCounter  implements Counter{
    long duration;
    TimeUnit unit;
    int partNum;
    long created;

    long milsPerBulk;

    ArrayList<CounterNode> buckets = new ArrayList();

    private int currentIndex = 0;
    private int next(){
        return (currentIndex + 1) % partNum;
    }

    private void init(){
        Preconditions.checkArgument(partNum>0);
        Preconditions.checkArgument(unit!=null);
        Preconditions.checkArgument(duration>0);
        Stream.iterate(0,i->i+1).limit(partNum)
                .map(n->CounterNode.getInstance(n))
                .forEach(counter -> buckets.add(counter));
    }

    public static SlidingCounter of(CountConf conf){
        CountConf c = conf;
        SlidingCounter counter = new SlidingCounter();
        counter.partNum = c.getPartNum();
        counter.duration = c.getDuration();
        counter.unit = c.getUnit();
        counter.created = System.currentTimeMillis();
        counter.milsPerBulk = counter.duration * c.getUnit().getMillisecond()/c.getPartNum();
        counter.init();
        return counter;
    }

    private long term(){
        return (System.currentTimeMillis() - created)/(duration * unit.getMillisecond());
    }

    private int route(){
        return (int) (((System.currentTimeMillis() - created) - term()*(duration * unit.getMillisecond()))/milsPerBulk);
    }

    public SlidingCounter increment(){
        buckets.get(route()).increment(term());
        return this;
    }

    public Long sum(){
        long term = term();
        int currentIndex = route();
        return buckets.stream()
                .filter(counterNode -> counterNode.getTerm()==term||counterNode.getTerm()==term-1)
                .filter(counterNode -> !(counterNode.getTerm()==term-1&&counterNode.getIndex()<=currentIndex))
                .map(CounterNode::getCount)
                .mapToLong(LongAdder::sum).sum();
    }
}
