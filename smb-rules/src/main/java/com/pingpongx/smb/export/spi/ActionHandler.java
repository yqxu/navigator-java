package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.PipelineContext;

public interface ActionHandler<T> extends RuleHandler<T> {
    void doAction(T data);

    @Override
    default Object applyData(T data){
        doAction(data);
        return null;
    }

    default void doAction(T data, PipelineContext context){
        doAction(data);
    }
}
