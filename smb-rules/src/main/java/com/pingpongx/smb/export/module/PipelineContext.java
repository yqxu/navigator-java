package com.pingpongx.smb.export.module;


import java.util.HashMap;
import java.util.Map;


public class PipelineContext {
    MatchResult matchedHandler;
    String scene;
    Long count;
    Map<String,String> params = new HashMap<>();

    public static PipelineContext of(MatchResult result, String scene){
        PipelineContext context = new PipelineContext();
        context.setMatchedHandler(result);
        context.setScene(scene);
        return context;
    }

    public MatchResult getMatchedHandler() {
        return matchedHandler;
    }

    public void setMatchedHandler(MatchResult matchedHandler) {
        this.matchedHandler = matchedHandler;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
