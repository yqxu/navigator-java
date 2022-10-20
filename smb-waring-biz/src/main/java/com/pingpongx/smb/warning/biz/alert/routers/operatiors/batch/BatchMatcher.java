package com.pingpongx.smb.warning.biz.alert.routers.operatiors.batch;

import com.pingpongx.smb.warning.biz.alert.Identified;
import com.pingpongx.smb.warning.biz.alert.SortedIdentify;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;
import com.pingpongx.smb.warning.biz.moudle.FSMNode;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import com.pingpongx.smb.warning.biz.moudle.Node;
import com.pingpongx.smb.warning.biz.rules.RuleLeaf;

import java.util.HashSet;
import java.util.Set;

public interface BatchMatcher<T> extends Comparable<BatchMatcher<T>>, Identified<String> {
    Set<String> batchMatch(T input);

    MatchOperation supportedOperation();

    @Override
    default int compareTo(BatchMatcher<T> o) {
        return this.supportedOperation().sortBy() - o.supportedOperation().sortBy();
    }

    void putRule(RuleLeaf<?,T> rule);

}
