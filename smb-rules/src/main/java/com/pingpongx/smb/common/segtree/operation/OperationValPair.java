package com.pingpongx.smb.common.segtree.operation;


public class OperationValPair<ValType> {
    SegOperation<ValType> operation;
    ValType value;

    public static <ValType> OperationValPair  of(SegOperation<ValType> operation,ValType valType){
        OperationValPair<ValType> operationValPair = new OperationValPair<>();
        operationValPair.setOperation(operation);
        operationValPair.setValue(valType);
        return operationValPair;
    }

    public SegOperation<ValType> getOperation() {
        return operation;
    }

    public void setOperation(SegOperation<ValType> operation) {
        this.operation = operation;
    }

    public ValType getValue() {
        return value;
    }

    public void setValue(ValType value) {
        this.value = value;
    }
}
