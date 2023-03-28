package com.pingpongx.smb.export.module;

import com.pingpongx.smb.common.IdentityPath;
import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.common.Trie;
import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.operation.RuleAnd;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.operation.RuleOr;
import com.pingpongx.smb.export.spi.RuleHandler;
import com.pingpongx.smb.rule.routers.operatiors.Factories;
import com.pingpongx.smb.rule.routers.operatiors.batch.BatchMatcher;

import java.util.*;
import java.util.stream.Collectors;

public class RuleTrie extends Trie<RuleLeaf, RuleTrieElement> {
    Engine engine;

    public RuleTrie(Engine engine) {
        this.engine = engine;
    }

    public RuleTrie put(Rule rule, RuleHandler handler) {
        RuleOr or = rule.expansion();
        putOr(or, handler);
        return this;
    }

    public MatchResult match(Object data) {
        Set<Node<RuleLeaf, RuleTrieElement>> matchedRulesRepeat = new HashSet<>();
        MatchResult result = new MatchResult();
        TreeMap<BatchMatcher, MatchOperation> bfsQ = new TreeMap<>();
        getRoot().getData().getChildOperations().stream().forEach(operation -> {
            BatchMatcher matcher = engine.batchMatcherMapper.routeMatchers(operation);
            bfsQ.put(matcher, operation);
        });
        Set<MatchOperation> matchedOp = new HashSet<>();
        while (!bfsQ.isEmpty()) {
            Map.Entry<BatchMatcher, MatchOperation> entry = bfsQ.pollFirstEntry();
            Object attrVal = engine.extractor.getAttr(data, entry.getValue().attr());
            BatchMatcher matcher = entry.getKey();
            Set<Node<RuleLeaf, RuleTrieElement>> matchedRule = matcher.batchMatch(attrVal, matchedRulesRepeat);
            matchedRulesRepeat.addAll(matchedRule);
            matchedOp.add(entry.getValue());
            matchedRule.stream().flatMap(node -> node.getData().getChildOperations().stream()).filter(operation -> !matchedOp.contains(operation)).forEach(operation -> {
                BatchMatcher toAdd = engine.batchMatcherMapper.routeMatchers(operation);
                bfsQ.put(toAdd, operation);
            });
        }
        Set<RuleHandler> handlerSet = matchedRulesRepeat.stream().map(matched -> matched.getData()).filter(Objects::nonNull).map(element -> element.getHandlers()).filter(Objects::nonNull).flatMap(handlers -> handlers.stream()).collect(Collectors.toSet());
        result.setMatchedData(handlerSet);
        return result;
    }


    private RuleTrie putOnly(Rule rule, RuleHandler handler) {
        if (rule instanceof RuleAnd) {
            putAnd((RuleAnd) rule, handler);
        } else if (rule instanceof RuleOr) {
            putOr((RuleOr) rule, handler);
        } else {
            RuleAnd and = (RuleAnd) RuleAnd.newAnd(rule);
            putAnd(and, handler);
        }
        return this;
    }

    public RuleTrie putOr(RuleOr or, RuleHandler handler) {
        or.getOrRuleSet().stream().forEach(r -> putOnly(r, handler));
        return this;
    }
    private RuleTrie putAnd(RuleAnd and, RuleHandler handler) {
        List<RuleLeaf> ids = and.getAndRuleList().stream().map(r -> ((RuleLeaf) r)).collect(Collectors.toList());
        IdentityPath<RuleLeaf> path = IdentityPath.of(ids);
        Node<RuleLeaf, RuleTrieElement> node = getOrCreate(path);
        if (node.isNew()) {
            node.setData(RuleTrieElement.build());
            Node<RuleLeaf, RuleTrieElement> p = node.getParent();
            Node<RuleLeaf, RuleTrieElement> c = node;
            while (p != null) {
                if (p.isNew()) {
                    p.setData(RuleTrieElement.build());
                }
                p.getData().getChildOperations().add(c.getIdentify().operatorType());
                c = p;
                p = p.getParent();
            }
        }
        node.getData().getHandlers().add(handler);
        Map<RuleLeaf, Node<RuleLeaf, RuleTrieElement>> current = getRoot().getChildren();
        for (int i = 0; i < ids.size(); i++) {
            Node<RuleLeaf, RuleTrieElement> n = current.get(ids.get(i));
            RuleLeaf r = n.getIdentify();
            MatchOperation op = ((RuleLeaf<?>) r).operatorType();
            Node<String, BatchMatcher> matcherNode = engine.batchMatcherMapper.matchers(op);
            BatchMatcher matcher = matcherNode.getData();
            if (matcher == null) {
                matcher = Factories.Matcher.newBatchMatcher((String) op.getIdentify());
                matcherNode.setData(matcher);
            }
            matcher.putRule(r, n);
            current = n.getChildren();
        }
        return this;
    }

}
