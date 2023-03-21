package com.pingpongx.smb.export.module.persistance;


import com.alibaba.fastjson2.annotation.JSONType;
import com.pingpongx.smb.common.segtree.EndPoint;
import com.pingpongx.smb.common.segtree.Limit;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
public class Range implements Serializable {
    public static BigDecimal MAX=new BigDecimal(Long.MAX_VALUE);
    public static BigDecimal MIN=new BigDecimal(Long.MIN_VALUE);

    public static EndPoint MAX_ENDPOINT= EndPoint.of(MAX, Limit.LEFT_EQUAL);
    public static EndPoint MIN_ENDPOINT= EndPoint.of(MIN, Limit.RIGHT_EQUAL);

    String rangeType;
    String rangeStart;
    String rangeEnd;

    public RangeType getRangeType() {
        return RangeType.valueOf(rangeType);
    }

    public void setRangeType(String rangeType) {
        this.rangeType = rangeType;
    }

    public String getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(String rangeStart) {
        this.rangeStart = rangeStart;
    }

    public String getRangeEnd() {
        return rangeEnd;
    }

    public void setRangeEnd(String rangeEnd) {
        this.rangeEnd = rangeEnd;
    }

    @Override
    public String toString() {
        if (this.rangeType.equals(RangeType.LCRC)){
            return "["+rangeStart+","+rangeEnd+"]";
        }
        if (this.rangeType.equals(RangeType.LCRO)){
            return "["+rangeStart+","+rangeEnd+")";
        }
        if (this.rangeType.equals(RangeType.LORC)){
            return "("+rangeStart+","+rangeEnd+"]";
        }
        return "("+rangeStart+","+rangeEnd+")";
    }
}
