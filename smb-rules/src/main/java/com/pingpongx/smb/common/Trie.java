package com.pingpongx.smb.common;

import java.util.*;
import java.util.function.Supplier;

public class Trie<Key,Val> {
    Node<Key,Val> root = new Node();

    public Node<Key,Val> getRoot(){
        return root;
    }

    public Node<Key,Val> getNode(IdentityPath<Key> path){
        IdentityPath copy = path.deepCopy();
        return root.getChild(copy);
    }

    public Map<IdentityPath<Key>,Val> bfsGet(IdentityPath<Key> path){
        IdentityPath<Key> copy = path.deepCopy();
        Map<IdentityPath<Key>,Val> ret = new HashMap<>();
        Set<Node<Key,Val>> repeated = new HashSet<>();
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
                        if (repeated.add(keyValNode)){
                            newVisited.add(keyValNode);
                            if (keyValNode.getData()!=null){
                                ret.put(keyValNode.getPath(),keyValNode.getData());
                            }
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

    public Node<Key,Val> getOrCreateAndInit(IdentityPath<Key> path, Supplier<Val> defaultVal){
        IdentityPath<Key> copy = path.deepCopy();
        return root.getOrCreate(copy,defaultVal);
    }

    public Trie<Key,Val> put(IdentityPath<Key> path,Val value){
        Node node = getOrCreate(path);
        node.setData(value);
        return this;
    }
}
