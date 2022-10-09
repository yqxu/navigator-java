package com.pingpongx.smb.warning.biz.alert;

import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import lombok.Data;

import java.util.PriorityQueue;

public class SortedIdentifies<I> {
    PriorityQueue<SortedIdentify<I>> ids = new PriorityQueue<>();

    public SortedIdentify<I> poll(){
        return ids.poll();
    }
    public SortedIdentify<I> peek(){
        return ids.peek();
    }
}
