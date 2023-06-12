package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.Identified;
import com.pingpongx.smb.export.module.PipelineContext;

import java.util.List;

/***
 * 继承 Identified 用于remove的时候同set下，做handler 区分
 * @param <T>
 */
public interface Sequential extends Identified<String> {

    /**
     * 哪个链下的处理节点，处理节点可以被多个链使用返回是List
     * @return
     */
    List<String> chainsOf();

    /***
     * 在链中如果需要排序，按照这个方法的返回排序
     * @return
     */
    default int priority(){
        return 0;
    }

}
