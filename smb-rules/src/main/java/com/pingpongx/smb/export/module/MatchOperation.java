package com.pingpongx.smb.export.module;


import com.pingpongx.smb.rule.routers.operatiors.batch.BatchMatcher;

public interface MatchOperation<T> extends Identified<String>, Comparable<MatchOperation<T>> {
    /****
     * 操作符在字典树中的排序，排序考前则先被运算（batch 运算）
     * 越能够缩小查找范围的优先级应该越高
     * batch 优化越差优先级越低 O（1）=0 , O（n）=100 , O（n*m）= 9900 , O（n^2）= 10000 (n=100 m=99 代入得到)
     *
     * 0 为优先级最高，数字越大优先级越小，同优先级下按照identify 字母序排序。
     * @return
     */
    int sortBy();

    String obj();

    String attr();

    MatchOperation obj(String obj);

    MatchOperation attr(String attr);

    @Override
    default int compareTo(MatchOperation<T> o) {
        int ret = this.sortBy()-o.sortBy();
        if (ret == 0){
            ret = this.attr().compareTo(o.attr());
        }
        return ret;
    }
}
