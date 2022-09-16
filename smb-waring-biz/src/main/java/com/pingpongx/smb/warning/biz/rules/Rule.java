package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.moudle.dingding.FireResultInfo;

public interface Rule<T> {
    boolean contentMatch(T content);

}
