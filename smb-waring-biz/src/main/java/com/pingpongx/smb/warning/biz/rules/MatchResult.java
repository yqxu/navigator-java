package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.moudle.Node;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MatchResult {
    Map<String,Map<String,RuleHandler>>  matchedData;

}
