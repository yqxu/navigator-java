package com.pingpongx.smb.warning.biz.alert.counter;

import java.util.Map;

public class TrieNode<T> {
    T data;
    Map<String,TrieNode> children;
    boolean hasLeafData = false;

    private TrieNode addChild(TriePath path,TrieNode<T> child){
        children.put(path.top(),child);
        return this;
    }

    public T getData() {
        return data;
    }

    public boolean hasLeafData(){
        return hasLeafData;
    }
}
