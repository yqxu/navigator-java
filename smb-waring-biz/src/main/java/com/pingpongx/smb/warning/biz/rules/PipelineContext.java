package com.pingpongx.smb.warning.biz.rules;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
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
}
