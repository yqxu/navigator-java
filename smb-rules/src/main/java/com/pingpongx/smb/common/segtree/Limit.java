package com.pingpongx.smb.common.segtree;

public enum Limit {
    //左极限逼近，等于，右极限逼近
    LEFT(-1),EQUAL(0),RIGHT(1);
    int val;
    Limit(int val) {
        this.val = val;
    }

}
