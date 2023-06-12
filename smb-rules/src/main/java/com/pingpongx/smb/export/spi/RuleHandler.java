package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.Identified;
import com.pingpongx.smb.export.module.PipelineContext;

import java.util.List;

/***
 * 继承 Identified 用于remove的时候同set下，做handler 区分
 * @param <T>
 */
public interface RuleHandler<T> extends Identified<String> {
    /***
     * handler 本身的业务特性，用于从匹配集合中筛选出想要的指定handler
     * @return
     */
    List<String> tags();
}
