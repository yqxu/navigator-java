package com.pingpongx.smb.common;

import com.pingpongx.smb.export.module.Identified;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

public class Node<Key,Val> implements Identified {
     Key identify;

     TreeMap<Key , Node<Key,Val>> children = new TreeMap<>();

     Node<Key,Val> parent;
     Val data;

     public static <Key,Val> Node<Key,Val> buildNode(Key identify){
         Node<Key,Val> node = new Node();
         node.identify = identify;
         return node;
     }

    public static <Key,Val> Node<Key,Val> buildNode(Key identify,Supplier<Val> defaultVal){
        Node<Key,Val> node = new Node();
        node.identify = identify;
        node.data = defaultVal.get();
        return node;
    }

    @Override
    public Key getIdentify() {
        return identify;
    }

    public Node<Key,Val> getChild(IdentityPath<Key> path){
        if (path.size() == 0){
            return this;
        }
        Node nextLevel = children.get(path.pop());
        if (nextLevel == null){
            return null;
        }
        return nextLevel.getChild(path);
    }

    public Node<Key,Val> getChildOnCurrentLevel(Key key){
        return children.get(key);
    }

    public Map<Key , Node<Key,Val>> getChildren(){
        return children;
    }

    public Node<Key,Val> getOrCreate(IdentityPath<Key> path){
        if (path.size() == 0){
            return this;
        }
        Key current =  path.pop();
        Node nextLevel = children.get(current);
        if (nextLevel == null){
            children.put(current,buildNode(current));
            nextLevel = children.get(current);
            nextLevel.setParent(this);
        }
        return nextLevel.getOrCreate(path);
    }

    public Node<Key,Val> getOrCreate(IdentityPath<Key> path, Supplier<Val> defaultVal){
        if (path.size() == 0){
            return this;
        }
        Key current =  path.pop();
        Node nextLevel = children.get(current);
        if (nextLevel == null){
            children.put(current,buildNode(current,defaultVal));
            nextLevel = children.get(current);
            nextLevel.setParent(this);
        }
        return nextLevel.getOrCreate(path,defaultVal);
    }

    public Val getData() {
        return data;
    }

    public void setData(Val data) {
        this.data = data;
    }

    public boolean isNew(){
         return data == null;
    }

    public Node<Key, Val> getParent() {
        return parent;
    }

    public void setParent(Node<Key, Val> parent) {
        this.parent = parent;
    }

    public IdentityPath<Key> getPath(){
         Node<Key,Val> current = this;
         IdentityPath<Key> path = new IdentityPath<>();
         path.push(current.getIdentify());
         while(current.getParent()!=null){
             current = current.getParent();
             if (current.getIdentify()!=null){
                 //root 节点identify 为空需要排除
                 path.push(current.getIdentify());
             }
         }
         return path;
    }
}
