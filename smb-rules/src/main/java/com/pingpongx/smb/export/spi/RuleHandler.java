package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.Identified;
import com.pingpongx.smb.export.module.PipelineContext;

public interface RuleHandler<T> extends Identified<String> {
    void handleMatchedData(T data, PipelineContext context);
    String scene();

}
