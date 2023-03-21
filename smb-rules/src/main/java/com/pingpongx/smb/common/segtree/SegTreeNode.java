package com.pingpongx.smb.common.segtree;

import com.pingpongx.smb.common.segtree.operation.OperationValPair;
import com.pingpongx.smb.common.segtree.operation.SegOperation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SegTreeNode<ValType> {
    Segment segment;

    SegTreeNode<ValType> left;
    SegTreeNode<ValType> right;

    List<OperationValPair<ValType>> lazy = new ArrayList<>();
    ValType value;
    int pointCnt = 0;

    public Segment getSegment() {
        return segment;
    }

    public void setSegment(Segment segment) {
        this.segment = segment;
    }

    private void pushDown(){
        lazy.stream().forEach(pair -> {
            left.doOperation(pair.getOperation(),left.getSegment(),pair.getValue());
            right.doOperation(pair.getOperation(),right.getSegment(),pair.getValue());
        });
        this.lazy = new ArrayList<>();
    }

    public void split(EndPoint endPoint){
        if (endPoint.getVal().compareTo(this.segment.getStart().getVal())<0||endPoint.getVal().compareTo(this.segment.getEnd().getVal())>0){
            //无交集 忽略
            return;
        }
        if (endPoint.compareTo(segment.getStart())==0&&endPoint.limit.equals(segment.getStart().limit)){
            //节点同左端，不需要分裂
            return;
        }else if (endPoint.compareTo(segment.getEnd())==0&&endPoint.limit.equals(segment.getEnd().limit)){
            //节点同右端，不需要分裂
            return;
        }
        if (left == null&&right==null){
            //节点为末端叶子，实际拆分
                //获取 互补点
            EndPoint reversal = endPoint.reversal();
            left = new SegTreeNode<>();
            right = new SegTreeNode<>();
            if (endPoint.limit.equals(Limit.LEFT)||endPoint.limit.equals(Limit.LEFT_EQUAL)){
                left.segment = Segment.of(segment.getStart(),endPoint);
                right.segment = Segment.of(reversal,segment.getEnd());
            }else{
                left.segment = Segment.of(segment.getStart(),reversal);
                right.segment = Segment.of(endPoint,segment.getEnd());
            }
            this.pushDown();
            left.calculateLazy();
            right.calculateLazy();
            this.value = null;
        }
        if (left.getSegment().getEnd().compareTo(endPoint)>=0){
            left.split(endPoint);
        }else {
            right.split(endPoint);
        }
    }

    public void doOperation(SegOperation<ValType> operation,Segment segment , ValType val){
        if (!this.hasIntersection(segment)){
            //无交集忽略
            return;
        }
        if (segment.getStart().compareTo(this.segment.start)<=0&&segment.getEnd().compareTo(this.segment.end)>=0){
            //全覆盖，lazy缓存
            lazy.add(OperationValPair.of(operation,val));
            return;
        }
        //部分覆盖
        if (left==null||right==null){
            this.split(segment.getStart());
            this.split(segment.getEnd());
        }
        if (left.hasIntersection(segment)){
            left.doOperation(operation,segment,val);
        }
        if (right.hasIntersection(segment)){
            right.doOperation(operation,segment,val);
        }
    }

    public boolean hasIntersection(Segment segment){
        EndPoint start ;
        if (this.segment.getStart().compareTo(segment.getStart())>=0){
            start = this.segment.getStart();
        }else {
            start = segment.getStart();
        }
        EndPoint end;
        if (this.segment.getEnd().compareTo(segment.getEnd())>=0){
            end = segment.getStart();
        }else {
            end = this.segment.getStart();
        }
        return end.compareTo(start)>=0;
    }
    public static <ValType> SegTreeNode<ValType> newRoot(){
        SegTreeNode<ValType> segTreeNode = new SegTreeNode<>();
        segTreeNode.setSegment(Segment.fullRange());
        return segTreeNode;
    }

    public ValType valuesOfPoint(BigDecimal point){
        if (!segment.isPointIn(point)){
            return null;
        }
        if (this.left == null && right == null){
            return calculateLazy();
        }
        if (left.getSegment().isPointIn(point)){
            return left.valuesOfPoint(point);
        }
        if (right.getSegment().isPointIn(point)){
            return right.valuesOfPoint(point);
        }
        return null;
    }

    public ValType calculateLazy(){
        lazy.forEach(e->this.value = e.getOperation().doMerge(e.getValue(),value));
        return this.value;
    }
}
