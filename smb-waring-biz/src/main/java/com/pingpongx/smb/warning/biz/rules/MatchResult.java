package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.moudle.Node;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class MatchResult {
    Set<RuleHandler> matchedData;

}
