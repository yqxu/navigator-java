package com.pingpongx.smb.common;

public class Rete<Key,Val> {
    Node<Key,Val> root = new Node();

    public Node<Key,Val> getRoot(){
        return root;
    }

    public Node<Key,Val> getNode(IdentityPath<Key> path){
        IdentityPath copy = path.deepCopy();
        return root.getChild(copy);
    }

    public Node<Key,Val> getOrCreate(IdentityPath<Key> path){
        IdentityPath<Key> copy = path.deepCopy();
        return root.getOrCreate(copy);
    }

    public Rete<Key,Val> put(IdentityPath<Key> path, Val value){
        Node node = getOrCreate(path);
        node.setData(value);
        return this;
    }
}
