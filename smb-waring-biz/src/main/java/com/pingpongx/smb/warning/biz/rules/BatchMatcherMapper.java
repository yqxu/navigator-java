package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch.BatchMatcher;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.moudle.Node;
import com.pingpongx.smb.warning.biz.moudle.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BatchMatcherMapper {
    Trie<String, TreeSet<BatchMatcher>> ruleTries = new Trie<>();

    @Autowired
    DataAttrMapper attrMapper;
    TreeSet<BatchMatcher> matchers(String type, String attr){
        IdentityPath<String> path = IdentityPath.of(Stream.of(type,attr).collect(Collectors.toList()));
        Node<String,TreeSet<BatchMatcher>> node = ruleTries.getOrCreate(path);
        if (node.isNew()){
            node.setData(new TreeSet<>());
        }
        return node.getData();
    }

    public BatchMatcherMapper put(String type,String attr,BatchMatcher matcher){
        matchers(type,attr).add(matcher);
        attrMapper.put(type,attr);
        return this;
    }


    public TreeSet<BatchMatcher> routeMatchers(Object data,String attr){
        String type = data.getClass().getSimpleName();
        return matchers(type,attr);
    }
}
