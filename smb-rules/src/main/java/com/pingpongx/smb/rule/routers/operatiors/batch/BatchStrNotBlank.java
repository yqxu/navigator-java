package com.pingpongx.smb.rule.routers.operatiors.batch;

import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.rule.routers.operatiors.StrEquals;
import com.pingpongx.smb.rule.routers.operatiors.StrNotBlank;

import java.util.Set;
import java.util.stream.Collectors;


public class BatchStrNotBlank extends BatchNotEmpty<String> {
    @Override
    public MatchOperation supportedOperation() {
        return StrNotBlank.getInstance();
    }

    public static SameTypeMatcher<String> newInstance() {
        BatchStrNotBlank strContains = new BatchStrNotBlank();
        return strContains;
    }

    @Override
    public Set<Node<RuleLeaf, RuleTrieElement>> batchMatch(Object input, Set<Node<RuleLeaf, RuleTrieElement>> repeat) {
        if (input==null||input.toString().isEmpty()) {
            return emptySet.stream().collect(Collectors.toSet());
        } else {
            return notEmptySet.stream().collect(Collectors.toSet());
        }
    }
}
