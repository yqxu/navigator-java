package com.pingpongx.smb.rule.routers.operatiors.batch;

import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.Identified;
import com.pingpongx.smb.export.module.MatchOperation;


import java.util.Set;

public interface BatchMatcher<T> extends Comparable<BatchMatcher<T>>, Identified<String> {
    Set<Node<RuleLeaf, RuleTrieElement>> batchMatch(T input,Set<Node<RuleLeaf, RuleTrieElement>> repeat);

    MatchOperation supportedOperation();

    @Override
    default int compareTo(BatchMatcher<T> o) {
        return this.supportedOperation().sortBy() - o.supportedOperation().sortBy();
    }

    void putRule(RuleLeaf<?,T> rule, Node<RuleLeaf, RuleTrieElement> node);

}
