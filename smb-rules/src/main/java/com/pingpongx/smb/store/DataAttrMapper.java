package com.pingpongx.smb.store;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class DataAttrMapper {
    Map<String, SortedSet<String>> attrMapper = new ConcurrentHashMap<>();

    public Set<String> attrsOf(String type){
        return attrMapper.get(type);
    }

    public DataAttrMapper put(String type, String attr){
        SortedSet<String> set = attrMapper.get(type);
        if (set == null){
            set = new TreeSet<>();
            attrMapper.put(type,set);
        }
        set.add(attr);
        return this;
    }

}
