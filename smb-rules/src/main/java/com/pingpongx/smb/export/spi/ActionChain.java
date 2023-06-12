package com.pingpongx.smb.export.spi;

import com.pingpongx.smb.export.module.MatchResult;
import com.pingpongx.smb.export.module.PipelineContext;

import java.util.Comparator;
import java.util.List;

public abstract class ActionChain<T> implements ActionHandler<T> {
    public PipelineContext generateContext(MatchResult result){
        context = PipelineContext.of(result);
        return context;
    }
    volatile PipelineContext context;
    List<ChainedActionHandler> sequentialList;
    @Override
    public void doAction(T data){
        if (context == null){
            throw new RuntimeException("context not init.");
        }
        sequentialList.stream()
                .sorted(Comparator.comparingInt(ChainedActionHandler::priority))
                .forEach(chainedActionHandler ->chainedActionHandler.doAction(data,context));
    }
}
