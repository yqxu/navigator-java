package com.pingpongx.smb.warning.biz.alert.counter;

import java.util.concurrent.atomic.LongAdder;

public interface Counter {
    Counter increment();
    long sum();
}
