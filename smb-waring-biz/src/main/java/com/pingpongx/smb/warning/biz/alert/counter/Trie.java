package com.pingpongx.smb.warning.biz.alert.counter;

public class Trie<T> {
    TrieNode<T> root;

    TrieNode<T> getNode(TriePath path){
        return root;
    }

    T getData(TriePath path){
        return root.getData();
    }

    void put(TriePath path,T data){

    }
}
