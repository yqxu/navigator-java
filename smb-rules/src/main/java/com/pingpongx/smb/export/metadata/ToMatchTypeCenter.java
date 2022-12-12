package com.pingpongx.smb.export.metadata;

import java.util.concurrent.ConcurrentHashMap;

public class ToMatchTypeCenter{
    public ConcurrentHashMap<String,Class> metadatas = new ConcurrentHashMap();
    public void reg(Class clazz){
        if (metadatas.get(clazz)!=null){
            throw new RuntimeException("conflict simple name of dataType.");
        }
        metadatas.put(clazz.getSimpleName(),clazz);
    }

    public Class get(String name){
        return metadatas.get(name);
    }

}
