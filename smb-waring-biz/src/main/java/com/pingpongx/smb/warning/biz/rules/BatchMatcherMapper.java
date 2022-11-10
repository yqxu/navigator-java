package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch.BatchMatcher;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.moudle.Node;
import com.pingpongx.smb.warning.biz.moudle.Trie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class BatchMatcherMapper {
    Trie<String, TreeMap<String,BatchMatcher>> ruleTries = new Trie<>();

    @Autowired
    DataAttrMapper attrMapper;

    TreeMap<String,BatchMatcher> matchers(String type, String attr){
        IdentityPath<String> path = IdentityPath.of(Stream.of(type,attr).collect(Collectors.toList()));
        Node<String, TreeMap<String,BatchMatcher>> node = ruleTries.getOrCreate(path);
        log.info("type:"+type+" attr:"+attr+" node:\n"+node);
        if (node.isNew()){
            node.setData(new TreeMap<>());
        }
        return node.getData();
    }

    public BatchMatcherMapper put(String type,String attr,BatchMatcher matcher){
        matchers(type,attr).put((String) matcher.getIdentify(),matcher);
        attrMapper.put(type,attr);
        return this;
    }


    public TreeMap<String,BatchMatcher> routeMatchers(Object data,String attr){
        String type = data.getClass().getSimpleName();
        return matchers(type,attr);
    }

}
