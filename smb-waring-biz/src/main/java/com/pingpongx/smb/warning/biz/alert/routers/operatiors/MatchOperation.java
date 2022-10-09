package com.pingpongx.smb.warning.biz.alert.routers.operatiors;

import com.pingpongx.smb.warning.biz.alert.Identified;

import java.util.Set;

public interface MatchOperation<T> extends Identified {
    /****
     * 操作符在字典树中的排序，排序考前则先被运算（batch 运算）
     * 越能够缩小查找范围的优先级应该越高
     * batch 优化越差优先级越低 O（1）=0 , O（n）=100 , O（n*m）= 9900 , O（n^2）= 10000 (n=100 m=99 代入得到)
     *
     * 0 为优先级最高，数字越大优先级越小，同优先级下按照identify 字母序排序。
     * @return
     */
    int sortBy();

    default boolean logicOfNot(){
        return false;
    }
}
