package com.pingpongx.smb.debug;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.export.module.operation.RuleLeaf;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumDeltaDebugHandler<D> implements DebugHandler<D, BigDecimal> {
    private final RuleLeaf ruleLeaf;
    private final Engine engine;

    public NumDeltaDebugHandler(RuleLeaf ruleLeaf, Engine engine) {
        this.ruleLeaf = ruleLeaf;
        this.engine = engine;
    }

    @Override
    public String getIdentify() {
        return ruleLeaf.getIdentify();
    }


    @Override
    public List<String> tags() {
        return Stream.of("debug").collect(Collectors.toList());
    }

    @Override
    public BigDecimal applyData(D data, PipelineContext context) {
        BigDecimal exp = new BigDecimal(ruleLeaf.expected().toString());
        BigDecimal real = new BigDecimal(engine.extractor.getAttr(data, ruleLeaf.dependsAttr()).toString());
        return exp.subtract(real);
    }
}
