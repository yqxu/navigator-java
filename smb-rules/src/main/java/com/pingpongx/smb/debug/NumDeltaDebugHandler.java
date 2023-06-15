package com.pingpongx.smb.debug;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.persistance.Range;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NumDeltaDebugHandler<D> implements DebugHandler<D, BigDecimal> {
    private  RuleLeaf<Range> ruleLeaf;
    private Engine engine;

    public NumDeltaDebugHandler() {

    }

    @Override
    public String getIdentify() {
        return ruleLeaf.getIdentify();
    }


    @Override
    public List<String> tags() {
        return Stream.of("debug","number").collect(Collectors.toList());
    }

    @Override
    public BigDecimal applyData(D data) {
        Range exp = ruleLeaf.expected();
        BigDecimal start = new BigDecimal(exp.getRangeStart());
        BigDecimal end = new BigDecimal(exp.getRangeEnd());
        BigDecimal real = new BigDecimal(engine.extractor.getAttr(data, ruleLeaf.dependsAttr()).toString());
        BigDecimal deltaStart = real.subtract(start);
        BigDecimal deltaEnd =  end.subtract(real);
        return deltaStart.min(deltaEnd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumDeltaDebugHandler<?> that = (NumDeltaDebugHandler<?>) o;

        if (ruleLeaf != null ? !ruleLeaf.equals(that.ruleLeaf) : that.ruleLeaf != null) return false;
        return engine != null ? engine.equals(that.engine) : that.engine == null;
    }

    @Override
    public int hashCode() {
        int result = ruleLeaf != null ? ruleLeaf.hashCode() : 0;
        result = 31 * result + (engine != null ? engine.hashCode() : 0);
        return result;
    }

    @Override
    public DebugHandler<D, BigDecimal> setRuleLeaf(RuleLeaf ruleLeaf) {
        this.ruleLeaf = ruleLeaf;
        return this;
    }

    @Override
    public DebugHandler<D, BigDecimal> setEngine(Engine engine) {
        this.engine = engine;
        return this;
    }

    @Override
    public DebugHandler<D, BigDecimal> deepCopy() {
        NumDeltaDebugHandler handler = new NumDeltaDebugHandler();
        handler.setRuleLeaf(ruleLeaf);
        handler.setEngine(engine);
        return handler;
    }
}
