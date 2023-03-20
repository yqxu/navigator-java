package com.pingpongx.smb.rule.routers.operatiors.batch;

import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.common.segtree.SegTree;
import com.pingpongx.smb.common.segtree.Segment;
import com.pingpongx.smb.export.RuleConstant;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.export.module.RuleTrieElement;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.persistance.Range;
import com.pingpongx.smb.rule.routers.operatiors.StrEquals;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//TODO : 未完成开发将用于计费引擎，已完成引擎重构
public class BatchNumRange implements BatchMatcher<BigDecimal,Range> {
//    Map<RuleConstant.Operations>
    SegTree<Set<Node<RuleLeaf, RuleTrieElement>>> ranges = new SegTree<>();
    Set<Node<RuleLeaf, RuleTrieElement>> nullMatchedSet = new HashSet<>();


//

    /****
     * 批量范围匹配 提供批量O（logn）支撑
     * @param input 对象的待匹配属性，因为操作符的限制只能是string
     * @return 用了contains 规则的rule 的 identify 集合
     */
    @Override
    public Set<Node<RuleLeaf, RuleTrieElement>> batchMatch(Object input, Set<Node<RuleLeaf, RuleTrieElement>> repeat) {
        if (input == null){
            return nullMatchedSet;
        }
        BigDecimal val = new BigDecimal(input.toString());
        MatchedSet ret = new MatchedSet();
        ret.setMatchedNotRule(new HashSet<>());
        Set<Node<RuleLeaf, RuleTrieElement>> matched = Optional.ofNullable(ranges.valuesOfPoint(val)).orElse(new HashSet<>());
        ret.setMatchedRule(matched);
        return ret.getResult(repeat,null);
    }

    @Override
    public MatchOperation supportedOperation() {
        return StrEquals.getInstance();
    }

    @Override
    public void putRule(RuleLeaf<Range> rule, Node<RuleLeaf, RuleTrieElement> node) {
        Range exp = rule.expected();
        Segment aRange = Segment.of(exp);
        List<Segment> rangeList = new ArrayList<>();
        if (rule.isNot()){
            rangeList.add(Segment.of(Range.MIN_ENDPOINT,aRange.getStart().reversal()));
            rangeList.add(Segment.of(aRange.getEnd().reversal(),Range.MAX_ENDPOINT));
            nullMatchedSet.add(node);
        }else{
            rangeList.add(aRange);
        }
        rangeList.forEach(r->ranges.executeOperation(r, Stream.of(node).collect(Collectors.toSet()),(newOne,oldOne)->{
            oldOne.addAll(newOne);
            return oldOne;
        }));
    }


    @Override
    public String getIdentify() {
        return (String) supportedOperation().getIdentify();
    }

    public static BatchMatcher<BigDecimal,Range> newInstance() {
        BatchNumRange strContains = new BatchNumRange();
        return strContains;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BatchNumRange that = (BatchNumRange) o;

        return getIdentify() != null ? getIdentify().equals(that.getIdentify()) : that.getIdentify() == null;
    }

    @Override
    public int hashCode() {
        return getIdentify() != null ? getIdentify().hashCode() : 0;
    }
}
