package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.PipelineContext;

import java.util.List;

public interface FunctionalHandler<T,R> extends RuleHandler<T> {
    R applyData(T data);

    default void doAction(T data){
        applyData(data);
    }

    default void doAction(T data,PipelineContext context){
        applyData(data);
    }
}
