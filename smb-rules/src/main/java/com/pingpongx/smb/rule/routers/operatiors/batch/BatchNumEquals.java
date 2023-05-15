package com.pingpongx.smb.rule.routers.operatiors.batch;


public class BatchNumEquals extends BatchEquals<Number> {

    public static SameTypeMatcher<Number> newInstance() {
        BatchNumEquals strContains = new BatchNumEquals();
        return strContains;
    }
}
