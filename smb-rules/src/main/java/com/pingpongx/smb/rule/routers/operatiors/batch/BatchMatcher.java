package com.pingpongx.smb.rule.routers.operatiors.batch;

import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.Identified;
import com.pingpongx.smb.export.module.MatchOperation;


import java.util.Set;

public interface BatchMatcher<T,ConfT> extends Comparable<BatchMatcher<T,ConfT>>, Identified<String> {
    Set<Node<RuleLeaf, RuleTrieElement>> batchMatch(Object input,Set<Node<RuleLeaf, RuleTrieElement>> repeat);

    MatchOperation supportedOperation();

    @Override
    default int compareTo(BatchMatcher<T,ConfT> o) {
        return this.supportedOperation().sortBy() - o.supportedOperation().sortBy();
    }

    void putRule(RuleLeaf<ConfT> rule, Node<RuleLeaf, RuleTrieElement> node);

}
