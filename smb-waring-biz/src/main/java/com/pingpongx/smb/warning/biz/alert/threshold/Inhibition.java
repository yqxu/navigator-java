package com.pingpongx.smb.warning.biz.alert.threshold;

public interface Inhibition<T> {
    InhibitionResultEnum needInhibition(T data);
}
