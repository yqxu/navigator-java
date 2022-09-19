package com.pingpongx.smb.warning.biz.rules;

public interface Rule<T> {
    boolean contentMatch(T content);

}
