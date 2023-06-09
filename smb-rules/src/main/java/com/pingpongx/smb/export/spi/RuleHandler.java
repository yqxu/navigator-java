package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.Identified;
import com.pingpongx.smb.export.module.PipelineContext;

/***
 * 继承 Identified 用于remove的时候同set下，做handler 区分
 * @param <T>
 */
public interface RuleHandler<T> extends Identified<String> {
    void handleMatchedData(T data, PipelineContext context);

    /****
     *
     * @return
     */
    String scene();

}
