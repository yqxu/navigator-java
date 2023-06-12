package com.pingpongx.smb.export.module;


import java.util.HashMap;
import java.util.Map;


public class PipelineContext {
    MatchResult matchedHandler;
    Map<String,String> params = new HashMap<>();

    public static PipelineContext of(MatchResult result){
        PipelineContext context = new PipelineContext();
        context.setMatchedHandler(result);
        context.setParams(new HashMap<>());
        return context;
    }

    public MatchResult getMatchedHandler() {
        return matchedHandler;
    }

    public void setMatchedHandler(MatchResult matchedHandler) {
        this.matchedHandler = matchedHandler;
    }



    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
