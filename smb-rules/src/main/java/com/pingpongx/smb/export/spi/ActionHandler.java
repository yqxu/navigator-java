package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.PipelineContext;

public interface ActionHandler<T> extends RuleHandler<T> {
    void doAction(T data);
}
