package com.pingpongx.smb.rule.routers.operatiors.batch;

import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.rule.routers.operatiors.StrEquals;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BatchStrEquals extends BatchEquals<String> {
    @Override
    public MatchOperation supportedOperation() {
        return StrEquals.getInstance();
    }

    public static BatchMatcher<String> newInstance() {
        BatchStrEquals strContains = new BatchStrEquals();
        return strContains;
    }
}
