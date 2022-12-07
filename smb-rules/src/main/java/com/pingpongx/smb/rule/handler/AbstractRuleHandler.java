package com.pingpongx.smb.rule.handler;

import com.pingpongx.smb.export.spi.EventPublisher;
import com.pingpongx.smb.export.spi.RuleHandler;
import com.pingpongx.smb.export.module.MatchResult;

public abstract class AbstractRuleHandler<T> implements RuleHandler<T> {

    private MatchResult handleContext;
    private EventPublisher publisher;
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

    public MatchResult getHandleContext() {
        return handleContext;
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
