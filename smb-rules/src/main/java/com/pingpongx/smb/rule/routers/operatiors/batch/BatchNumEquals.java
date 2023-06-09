package com.pingpongx.smb.rule.routers.operatiors.batch;


import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BatchNumEquals extends BatchEquals<Number> {

    public static SameTypeMatcher<Number> newInstance() {
        BatchNumEquals strContains = new BatchNumEquals();
        return strContains;
    }
    @Override
    public Set<Node<RuleLeaf, RuleTrieElement>> batchMatch(Object inputObj,Object input, Set<Node<RuleLeaf, RuleTrieElement>> repeat) {
        if (input == null) {
            return notSet.stream().collect(Collectors.toSet());
        }
        String numStr = input.toString();
        BigDecimal in = new BigDecimal(numStr);
        MatchedSet matchedSet = Optional.ofNullable(ruleMap.get(in)).orElse(new MatchedSet());
        return matchedSet.getResult(repeat, notSet);
    }
}
