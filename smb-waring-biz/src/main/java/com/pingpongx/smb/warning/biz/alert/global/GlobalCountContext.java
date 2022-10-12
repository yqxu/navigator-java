package com.pingpongx.smb.warning.biz.alert.global;

import com.pingpongx.smb.warning.biz.alert.counter.Counter;
import com.pingpongx.smb.warning.biz.alert.counter.SlidingCounter;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.moudle.Node;
import com.pingpongx.smb.warning.biz.moudle.Trie;
import org.springframework.stereotype.Component;

public class GlobalCountContext {
    private static Trie<String,Counter> globalCountContext = new Trie<>();

    public static Counter getCounter(IdentityPath<String> path){
        Node<String,Counter> node = globalCountContext.getOrCreate(path);
        Counter ret = node.getData();
        return ret;
    }

    public static void putCounter(IdentityPath<String> path, Counter counter){
        Node<String,Counter> node = globalCountContext.getOrCreate(path);
        node.setData(counter);
    }

}
