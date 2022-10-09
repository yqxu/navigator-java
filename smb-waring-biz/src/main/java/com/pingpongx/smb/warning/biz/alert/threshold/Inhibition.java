package com.pingpongx.smb.warning.biz.alert.threshold;

import com.pingpongx.smb.warning.biz.rules.MatchResult;
import com.pingpongx.smb.warning.biz.rules.RuleHandler;

public interface Inhibition<T> extends RuleHandler<T> {
    InhibitionResultEnum needInhibition(T data, MatchResult matchContext);
}
