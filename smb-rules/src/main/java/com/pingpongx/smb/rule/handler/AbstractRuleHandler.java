package com.pingpongx.smb.rule.handler;

import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.export.spi.*;
import com.pingpongx.smb.export.module.MatchResult;

public abstract class AbstractRuleHandler<T> implements ChainedActionHandler<T> {

    private MatchResult handleContext;
    protected String sceneIdentity;

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof RuleHandler)){
            return false;
        }
        return this.getIdentify().equals(((RuleHandler<?>) o).getIdentify());
    }

    @Override
    public int hashCode() {
        return getIdentify().hashCode();
    }

    public void setHandleContext(MatchResult handleContext) {
        this.handleContext = handleContext;
    }

    public String getSceneIdentity() {
        return sceneIdentity;
    }

    public void setSceneIdentity(String sceneIdentity) {
        this.sceneIdentity = sceneIdentity;
    }

    public String scene(){
        return sceneIdentity;
    }
}
