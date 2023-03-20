package com.pingpongx.smb.common.segtree;

import com.pingpongx.smb.export.module.persistance.Range;
import netscape.security.UserTarget;

import java.math.BigDecimal;

public class Segment implements Cloneable {
    EndPoint start;
    EndPoint end;

    public EndPoint getStart() {
        return start;
    }

    public void setStart(EndPoint start) {
        this.start = start;
    }

    public EndPoint getEnd() {
        return end;
    }

    public void setEnd(EndPoint end) {
        this.end = end;
    }

    public static Segment of(EndPoint start,EndPoint end){
        Segment segment = new Segment();
        if (start.compareTo(end)>0){
            segment.setStart(start);
            segment.setEnd(end);
        }else {
            segment.setStart(end);
            segment.setEnd(start);
        }
        return segment;
    }

    public static Segment of(Range range){
        return fromRange(range);
    }

    public static Segment fullRange(){
        EndPoint start = new EndPoint();
        start.setVal(new BigDecimal(Long.MIN_VALUE));
        start.setLimit(Limit.RIGHT_EQUAL);

        EndPoint end = new EndPoint();
        start.setVal(new BigDecimal(Long.MAX_VALUE));
        start.setLimit(Limit.LEFT_EQUAL);

        return Segment.of(start,end);
    }

    public boolean isPointIn(BigDecimal point){
        if (point.compareTo(this.start.getVal())<0){
            return false;
        }
        if (point.compareTo(this.start.getVal())==0&&this.start.getLimit().equals(Limit.RIGHT)){
            return false;
        }
        if (point.compareTo(this.end.getVal())==0&&this.end.getLimit().equals(Limit.LEFT)){
            return false;
        }
        if (point.compareTo(this.end.getVal())>0){
            return false;
        }
        return true;
    }

    public static Segment fromRange(Range range){
        EndPoint start = EndPoint.of(new BigDecimal(range.getRangeStart()),range.getRangeType().getStartLimitType());
        EndPoint end = EndPoint.of(new BigDecimal(range.getRangeEnd()),range.getRangeType().getEndLimitType());
        return Segment.of(start,end);
    }

}
