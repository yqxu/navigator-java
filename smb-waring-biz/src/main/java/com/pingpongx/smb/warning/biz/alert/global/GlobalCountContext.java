package com.pingpongx.smb.warning.biz.alert.global;

import com.pingpongx.smb.warning.biz.alert.counter.Counter;
import com.pingpongx.smb.warning.biz.alert.counter.SlidingCounter;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.moudle.Node;
import com.pingpongx.smb.warning.biz.moudle.Trie;
import org.springframework.stereotype.Component;

@Component
public class GlobalCountContext {
    Trie<String,Counter> globalCountContext;

    public Counter getCounter(IdentityPath<String> path){
        Node<String,Counter> node = globalCountContext.getOrCreate(path);
        Counter ret = node.getData();
        return ret;
    }

    public void putCounter(IdentityPath<String> path, Counter counter){
        Node<String,Counter> node = globalCountContext.getOrCreate(path);
        node.setData(counter);
    }

}
