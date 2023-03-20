package com.pingpongx.smb.common.segtree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Limit {

    //左极限逼近，等于向左，等于向右，右极限逼近
    LEFT(-2,-1),LEFT_EQUAL(-1,0),RIGHT_EQUAL(2,0),RIGHT(1,1);
    int val;
    int compareVal;
    static Map<Integer,Limit> cache = new ConcurrentHashMap<>();
    Limit(int val,int compareVal) {
        this.val = val;
        this.compareVal = compareVal;
    }
    public Limit reversal(){
        int target = -val;
        if (cache.size()!=Limit.values().length){
            Arrays.stream(Limit.values()).forEach(limit -> cache.put(limit.val,limit));
        }
        return cache.get(target);
    }

}
