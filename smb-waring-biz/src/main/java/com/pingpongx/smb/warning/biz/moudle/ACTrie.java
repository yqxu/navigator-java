package com.pingpongx.smb.warning.biz.moudle;

import java.util.*;
import java.util.stream.Stream;

public class ACTrie<Key,Val> extends Trie<Key,FSMNode<Key,Val>>{
    public void failBackRebuild(){
        Queue<Node<Key,FSMNode<Key,Val>>> bfsQueue = new LinkedList<>();
        Node<Key,FSMNode<Key,Val>> preNode = this.getRoot();
        if (preNode.getData()==null){
            preNode.setData(new FSMNode<>());
        }
        bfsQueue.add(preNode);
        while(!bfsQueue.isEmpty()){
            preNode = bfsQueue.poll();
            preNode.getData().setNodeRef(preNode);
            Map<Key , Node<Key,FSMNode<Key,Val>>> map =  preNode.getChildren();
            if (map == null||map.size() == 0){
                continue;
            }
            for (Map.Entry<Key , Node<Key,FSMNode<Key,Val>>> entry:map.entrySet()) {
                Key key = entry.getKey();
                entry.getValue().getData().setNodeRef(entry.getValue());
                FSMNode<Key,Val> backOffRef = preNode.getData().searchBackOffRef(key);
                entry.getValue().getData().setBackOffRef(backOffRef.getNodeRef());
            }
            bfsQueue.addAll(map.values());
        }
    }

    public List<Node<Key,FSMNode<Key,Val>>> walk(IdentityPath<Key> path){
        List<Node<Key,FSMNode<Key,Val>>> walkThrough = new ArrayList<>();
        Node<Key,FSMNode<Key,Val>> step = root;
        while(path.size()>0){
            Node<Key,FSMNode<Key,Val>> next = step.getData().step(path);
            walkThrough.add(next);
            step = next;
        }
        return walkThrough;
    }
}
