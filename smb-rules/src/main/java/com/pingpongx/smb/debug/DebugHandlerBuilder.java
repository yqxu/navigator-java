package com.pingpongx.smb.debug;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.operation.RuleLeaf;

@FunctionalInterface
public interface DebugHandlerBuilder {
    DebugHandler build(Engine engine, RuleLeaf rule);
}
