package com.pingpongx.smb.warning.biz.moudle;

import com.pingpongx.smb.warning.biz.alert.Identified;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Node<Key,Val> implements Identified {
     Key identify;

     Map<Key , Node<Key,Val>> children = new HashMap<>();

     Node<Key,Val> parent;
     Val data;

     public static <Key,Val> Node<Key,Val> buildNode(Key identify){
         Node<Key,Val> node = new Node();
         node.identify = identify;
         return node;
     }

    public static <Key,Val> Node<Key,Val> buildNode(Key identify,Val data){
        Node<Key,Val> node = new Node();
        node.identify = identify;
        node.data = data;
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
        log.info("path:"+path.size());
        if (path.size() == 0){
            return this;
        }
        Key current =  path.pop();
        log.info("path:"+current.toString());
        Node nextLevel = children.get(current);
        if (nextLevel == null){
            children.put(current,buildNode(current));
            nextLevel = children.get(current);
            nextLevel.setParent(this);
        }
        return nextLevel.getOrCreate(path);
    }

    public Node<Key,Val> getOrCreate(IdentityPath<Key> path,Val defaultVal){
        log.info("path:"+path.size());
        if (path.size() == 0){
            return this;
        }
        Key current =  path.pop();
        log.info("path:"+current.toString());
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
             path.push(current.getIdentify());
         }
         return path;
    }
}
