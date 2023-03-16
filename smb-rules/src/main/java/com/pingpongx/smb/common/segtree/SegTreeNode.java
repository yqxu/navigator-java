package com.pingpongx.smb.common.segtree;

import com.pingpongx.smb.common.FSMNode;
import com.pingpongx.smb.common.Trie;

import java.util.List;

public class SegTreeNode<ValType> {
    Segment segment;
    List<SegTreeNode> children;
    List<ValType> lazy;
    List<ValType> value;
    int pointCnt = 0;

}
