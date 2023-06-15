package com.pingpongx.smb.debug;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.persistance.Range;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShowDeviationDebugHandler<D> implements DebugHandler<D, Deviation> {
    private RuleLeaf<Range> ruleLeaf;
    private Engine engine;

    public ShowDeviationDebugHandler( ) {

    }

    @Override
    public String getIdentify() {
        return ruleLeaf.getIdentify();
    }


    @Override
    public List<String> tags() {
        return Stream.of("debug","String").collect(Collectors.toList());
    }

    @Override
    public Deviation applyData(D data) {
        Deviation deviation = new Deviation();
        deviation.setExpected(ruleLeaf.expected());
        deviation.setActual(engine.extractor.getAttr(data,ruleLeaf.dependsAttr()));
        return deviation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShowDeviationDebugHandler<?> that = (ShowDeviationDebugHandler<?>) o;

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
    public DebugHandler<D, Deviation> setRuleLeaf(RuleLeaf ruleLeaf) {
        this.ruleLeaf = ruleLeaf;
        return this;
    }
    @Override
    public DebugHandler<D, Deviation> setEngine(Engine engine) {
        this.engine = engine;
        return this;
    }

    @Override
    public DebugHandler<D, Deviation> deepCopy() {
        ShowDeviationDebugHandler handler = new ShowDeviationDebugHandler();
        handler.setRuleLeaf(ruleLeaf);
        handler.setEngine(engine);
        return handler;
    }
}
