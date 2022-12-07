package com.pingpongx.smb.common;


import com.pingpongx.smb.export.module.SortedIdentifies;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class IdentityPath<T> implements Serializable {
    public  static <I> IdentityPath<I> of(){
        IdentityPath<I> path = new IdentityPath();
        return path;
    }
    public  static <I> IdentityPath<I> of(SortedIdentifies<I> ids){
        IdentityPath<I> path = new IdentityPath();
        while (ids.peek()!=null){
            path.append(ids.poll().getIdentify());
        }
        return path;
    }
    public  static <I> IdentityPath<I> of(List<I> ids){
        IdentityPath<I> path = new IdentityPath();
        ids.stream().forEach(i -> path.append(i));
        return path;
    }

    public  static <I> IdentityPath<I> of(I[] ids){
        IdentityPath<I> path = new IdentityPath();
        Arrays.stream(ids).forEach(i -> path.append(i));
        return path;
    }
    LinkedList<T> pathList = new LinkedList<>();

    public static IdentityPath<Character> of(char[] chars) {
        IdentityPath<Character> path = new IdentityPath();
        for (int i=0;i<chars.length;i++){
            path.append(chars[i]);
        }
        return path;
    }

    public IdentityPath<T> append(T path){
        pathList.add(path);
        return this;
    }
    public IdentityPath<T> push(T path){
        pathList.add(0,path);
        return this;
    }
    public IdentityPath<T> appendAll(IdentityPath<T> path){
        pathList.addAll(path.pathList);
        return this;
    }

    public T top(){
        if (pathList.size()==0){
            return null;
        }
        return pathList.get(0);
    }

    public T pop(){
        if (pathList.size()==0){
            return null;
        }
        return pathList.remove(0);
    }

    public T peek(){
        if (pathList.size()==0){
            return null;
        }
        return pathList.get(0);
    }
    public IdentityPath<T> deepCopy(){
        IdentityPath<T> ret = new IdentityPath();
        ret.pathList.addAll(this.pathList);
        return ret;
    }

    public int size(){
        return pathList.size();
    }

    @Override
    public String toString() {
        return pathList.stream().map(k->k.toString()).collect(Collectors.joining("."));
    }
}
