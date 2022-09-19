package com.pingpongx.smb.warning.biz.alert.counter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TriePath {
    List<String> pathList ;

    public static TriePath of(String path){
        TriePath p = new TriePath();
        String[] pArr = path.split(".");
        p.pathList = Arrays.stream(pArr).collect(Collectors.toList());
        return p;
    }

    public String top(){
        if (pathList.size()==0){
            return null;
        }
        return pathList.get(0);
    }

    public TriePath pop(){
        if (pathList.size()==0){
            return this;
        }
        pathList.remove(0);
        return this;
    }


}
