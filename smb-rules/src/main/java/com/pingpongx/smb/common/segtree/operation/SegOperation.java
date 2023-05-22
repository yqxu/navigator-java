package com.pingpongx.smb.common.segtree.operation;

@FunctionalInterface
public interface SegOperation<ValType> {
    ValType doMerge(ValType newOne,ValType oldOne);
}
