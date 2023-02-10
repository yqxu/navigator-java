package com.pingpongx.smb.common.segtree;

public class EndPoint implements Comparable<EndPoint>{
    Number val;
    Limit limit;

    public Number getVal() {
        return val;
    }

    public void setVal(Number val) {
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

    @Override
    public int compareTo(EndPoint o) {
        //TODO:
        return 0;
    }
}
