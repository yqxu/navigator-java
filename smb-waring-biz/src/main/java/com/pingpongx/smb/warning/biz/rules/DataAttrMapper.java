package com.pingpongx.smb.warning.biz.rules;

import com.google.common.collect.ImmutableSet;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch.BatchMatcher;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.moudle.Node;
import com.pingpongx.smb.warning.biz.moudle.Trie;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataAttrMapper {
    Map<String, Set<String>> attrMapper = new ConcurrentHashMap<>();

    Set<String> attrsOf(String type){
        return attrMapper.get(type);
    }

    public DataAttrMapper put(String type, String attr){
        Set<String> set = attrMapper.get(type);
        if (set == null){
            set = new HashSet<>();
            attrMapper.put(type,set);
        }
        set.add(attr);
        return this;
    }

}
