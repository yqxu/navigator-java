package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.PipelineContext;

public interface ChainedActionHandler<T> extends RuleHandler<T>,Sequential {
    void doAction(T data, PipelineContext context);
    default void doAction(T data){

    }

    @Override
    default Object applyData(T data) {
        return null;
    }
}
