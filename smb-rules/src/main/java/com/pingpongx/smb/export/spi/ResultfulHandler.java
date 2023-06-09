package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.Identified;
import com.pingpongx.smb.export.module.PipelineContext;

import java.util.List;

public interface ResultfulHandler<T,R> extends RuleHandler<T> {
    R applyData(T data, PipelineContext context);
    /**
     * for filter handler in same result set.
     * @return
     */
    List<String> tags();
    @Deprecated
    default void handleMatchedData(T data, PipelineContext context){
        return;
    }

    @Deprecated
    @Override
    default String scene() {
        return null;
    }
}
