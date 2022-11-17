package com.pingpongx.smb.warning.biz.moudle;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Trie<Key,Val> {
    Node<Key,Val> root = new Node();

    public Node<Key,Val> getRoot(){
        return root;
    }

    public Node<Key,Val> getNode(IdentityPath<Key> path){
        IdentityPath copy = path.deepCopy();
        return root.getChild(copy);
    }

    public Map<String,Val> bfsGet(IdentityPath<Key> path){
        IdentityPath<Key> copy = path.deepCopy();
        Map<String,Val> ret = new HashMap<>();
        Set<Val> repeated = new HashSet<>();
        List<Node<Key,Val>> visited = new ArrayList<>();
        visited.add(getRoot());
        while(copy.size()>0){
            Key current = copy.pop();
            List<Node<Key,Val>> newVisited = new ArrayList<>();
            visited.stream()
                    .map(Node::getChildren)
                    .filter(Objects::nonNull)
                    .map(m->m.get(current))
                    .filter(Objects::nonNull)
                    .forEach(keyValNode -> {
                        if (repeated.add(keyValNode.getData())){
                            ret.put(keyValNode.getPath().toString(),keyValNode.getData());
                            newVisited.add(keyValNode);
                        }
                    });
            visited.addAll(newVisited);
        }
        return ret;
    }

    public Node<Key,Val> getOrCreate(IdentityPath<Key> path){
        IdentityPath<Key> copy = path.deepCopy();
        return root.getOrCreate(copy);
    }

    public Node<Key,Val> getOrCreate(IdentityPath<Key> path, Supplier<Val> defaultVal){
        IdentityPath<Key> copy = path.deepCopy();
        return root.getOrCreate(copy,defaultVal);
    }

    public Trie<Key,Val> put(IdentityPath<Key> path,Val value){
        Node node = getOrCreate(path);
        node.setData(value);
        return this;
    }
}
