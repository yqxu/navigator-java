package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.Identified;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;

public interface RuleHandler<T> extends Identified<String> {
    void handleMatchedData(T data,MatchResult matchContext);

    IdentityPath<String> getPath();
    void setPath(IdentityPath<String> path);

}
