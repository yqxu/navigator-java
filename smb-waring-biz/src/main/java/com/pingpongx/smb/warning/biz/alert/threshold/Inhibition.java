package com.pingpongx.smb.warning.biz.alert.threshold;

public interface Inhibition<T> {
    boolean needInhibition(T data);
}
