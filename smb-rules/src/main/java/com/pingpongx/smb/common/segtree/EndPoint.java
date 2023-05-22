package com.pingpongx.smb.common.segtree;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class EndPoint implements Comparable<EndPoint>,Cloneable{
    BigDecimal val;
    Limit limit;

    public BigDecimal getVal() {
        return val;
    }

    public void setVal(BigDecimal val) {
        this.val = val;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EndPoint endPoint = (EndPoint) o;

        if (!val.equals(endPoint.val)) return false;
        return limit == endPoint.limit;
    }

    @Override
    public int hashCode() {
        int result = val.hashCode();
        result = 31 * result + limit.hashCode();
        return result;
    }


    private static Map<Limit,Integer> compareVal = new HashMap<>();

    @Override
    public int compareTo(EndPoint o) {
        int ret = this.val.compareTo(o.val);
        if (ret!=0){
            return ret;
        }
        return this.limit.compareTo(o.limit);
    }

    public EndPoint reversal(){
        EndPoint ret = new EndPoint();
        ret.setVal(this.val);
        ret.setLimit(this.limit.reversal());
        return ret;
    }

    public static EndPoint of(BigDecimal val,Limit limit){
        EndPoint point = new EndPoint();
        point.setVal(val);
        point.setLimit(limit);
        return point;
    }
}
