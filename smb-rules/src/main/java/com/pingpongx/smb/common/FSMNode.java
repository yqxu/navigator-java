package com.pingpongx.smb.common;


/***
 * 带回朔指针的 Node 结构，用于构建有限状态自动机 （AC-FSM）AC自动机
 * @param <Key> 字典树路径构成的类型
 * @param <Val> 字典树存放的数据类型
 */

public class FSMNode<Key,Val> {
    Node<Key,FSMNode<Key,Val>> nodeRef;
    Node<Key,FSMNode<Key,Val>> backOffRef;
    Val data;

    public FSMNode<Key,Val> searchBackOffRef(Key key){
        if (backOffRef == null){
            //ROOT node
            return this;
        }
        if(backOffRef.getChildOnCurrentLevel(key)!=null){
            return backOffRef.getChildOnCurrentLevel(key).getData();
        }
        if (backOffRef.getParent()==null){
            //root
            return backOffRef.getData();
        }
        return backOffRef.getData().searchBackOffRef(key);
    }

    public Node<Key,FSMNode<Key,Val>> step(IdentityPath<Key> path){
        Key key = path.peek();
        if (nodeRef.children.get(key)!=null){
            path.pop();
            return nodeRef.children.get(key);
        }else if (backOffRef == null){
            //ROOT
            path.pop();
            return nodeRef;
        }else{
            return backOffRef;
        }
    }

    public Node<Key, FSMNode<Key, Val>> getNodeRef() {
        return nodeRef;
    }

    public void setNodeRef(Node<Key, FSMNode<Key, Val>> nodeRef) {
        this.nodeRef = nodeRef;
    }

    public Node<Key, FSMNode<Key, Val>> getBackOffRef() {
        return backOffRef;
    }

    public void setBackOffRef(Node<Key, FSMNode<Key, Val>> backOffRef) {
        this.backOffRef = backOffRef;
    }

    public Val getData() {
        return data;
    }

    public void setData(Val data) {
        this.data = data;
    }
}
