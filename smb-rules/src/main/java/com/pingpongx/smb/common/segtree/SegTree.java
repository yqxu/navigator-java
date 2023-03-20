package com.pingpongx.smb.common.segtree;

import com.pingpongx.smb.common.segtree.operation.SegOperation;

import java.math.BigDecimal;
import java.util.*;

public class SegTree<Val> {
    SegTreeNode<Val> segTreeRoot = SegTreeNode.newRoot();

//    TreeSet<EndPoint> endPointSet = new TreeSet<>(); //[-99389128,-289.123,43145,12351341345]

    public void executeOperation(Segment segment, Val val, SegOperation<Val> operation){
        segTreeRoot.doOperation(operation,segment, val);
    }

    public Val valuesOfPoint(BigDecimal point){
        return segTreeRoot.valuesOfPoint(point);
    }
    public Val valuesOfPoint(Long point){
        return segTreeRoot.valuesOfPoint(new BigDecimal(point));
    }

//    void reIndex(){
//        ArrayList<EndPoint> endPoints = new ArrayList<>();
//        SegTreeNode<Val> newRoot = SegTreeNode.newRoot();
//        while(!endPointSet.isEmpty()){
//            endPoints.add(endPointSet.pollFirst());
//        }
//        while(endPoints.isEmpty()){
//            newRoot.split(endPoints.remove(endPoints.size()/2));
//        }
//
//    }

}
