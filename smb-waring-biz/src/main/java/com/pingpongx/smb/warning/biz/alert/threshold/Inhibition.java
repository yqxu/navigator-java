package com.pingpongx.smb.warning.biz.alert.threshold;

import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.export.spi.RuleHandler;

public interface Inhibition<T> extends RuleHandler<T> {
    InhibitionResultEnum needInhibition(T data, PipelineContext matchContext);
}
