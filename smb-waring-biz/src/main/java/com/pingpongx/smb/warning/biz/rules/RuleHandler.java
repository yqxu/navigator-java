package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.Identified;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;

public interface RuleHandler<T> extends Identified<String> {
//    void handleMatchedData(T data,IdentityPath<String> path,MatchResult matchContext);
    void handleMatchedData(T data,PipelineContext context);
    String scene();

}
