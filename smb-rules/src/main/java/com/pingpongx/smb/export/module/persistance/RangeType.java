package com.pingpongx.smb.export.module.persistance;


import com.pingpongx.smb.common.segtree.Limit;

public enum RangeType {
    LORO(Limit.RIGHT,Limit.LEFT),LORC(Limit.RIGHT,Limit.LEFT_EQUAL),LCRO(Limit.RIGHT_EQUAL,Limit.LEFT),LCRC(Limit.RIGHT_EQUAL,Limit.LEFT_EQUAL);
    Limit startLimitType;
    Limit endLimitType;
    RangeType(Limit left, Limit right) {
        this.startLimitType = left;
        this.endLimitType = right;
    }

    public Limit getStartLimitType() {
        return startLimitType;
    }

    public Limit getEndLimitType() {
        return endLimitType;
    }
}
