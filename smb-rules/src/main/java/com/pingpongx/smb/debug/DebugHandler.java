package com.pingpongx.smb.debug;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.spi.FunctionalHandler;

public interface DebugHandler<D,R> extends FunctionalHandler<D,R> {
    DebugHandler<D,R> setRuleLeaf(RuleLeaf ruleLeaf);
    DebugHandler<D,R> setEngine(Engine engine);
    DebugHandler<D,R> deepCopy();
}
