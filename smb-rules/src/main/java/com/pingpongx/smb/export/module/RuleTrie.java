package com.pingpongx.smb.export.module;

import com.pingpongx.smb.common.IdentityPath;
import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.common.Trie;
import com.pingpongx.smb.debug.DebugHandler;
import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.operation.RuleAnd;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.operation.RuleOr;
import com.pingpongx.smb.export.spi.RuleHandler;
import com.pingpongx.smb.rule.routers.operatiors.Factories;
import com.pingpongx.smb.rule.routers.operatiors.batch.BatchMatcher;
import io.vavr.Tuple;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    //仅在beta 网络中移除对应handler，如果需要节省内存资源或者减少alpha网络产出的中间结果数，需要重新构建字典树。
    public RuleTrie remove(Rule rule, RuleHandler handler) {
        RuleOr or = rule.expansion();
        Set<Node<RuleLeaf, RuleTrieElement>> nodes = get(or);
        nodes.stream().forEach(node -> node.getData().removeHandler(handler));
        return this;
    }

    public Set<Node<RuleLeaf, RuleTrieElement>> get(Rule rule) {
        RuleOr or = rule.expansion();
        return orGet(or).collect(Collectors.toSet());
    }

    private Stream<Node<RuleLeaf, RuleTrieElement>> orGet(RuleOr or) {
        return or.getOrRuleSet().stream().flatMap(r -> getOnly(r));
    }

    private Stream<Node<RuleLeaf, RuleTrieElement>> getOnly(Rule rule){
        if (rule instanceof RuleAnd) {
            return andGet((RuleAnd) rule);
        } else if (rule instanceof RuleOr) {
            return orGet((RuleOr) rule);
        }
        RuleAnd and = (RuleAnd) RuleAnd.newAnd(rule);
        return andGet(and);

    }

    public Set<Node<RuleLeaf, RuleTrieElement>> innerMatch(Object data) {
        Set<Node<RuleLeaf, RuleTrieElement>> matchedRulesRepeat = new HashSet<>();
        TreeMap<BatchMatcher, MatchOperation> bfsQ = new TreeMap<>();
        Optional<TreeSet<MatchOperation>> op = Optional.of(getRoot()).map(r->r.getData()).map(d->d.getChildOperations());
        if (!op.isPresent()){
            return matchedRulesRepeat;
        }
        op.get().stream().forEach(operation -> {
            BatchMatcher matcher = engine.batchMatcherMapper.routeMatchers(operation);
            bfsQ.put(matcher, operation);
        });
        Set<MatchOperation> matchedOp = new HashSet<>();
        while (!bfsQ.isEmpty()) {
            Map.Entry<BatchMatcher, MatchOperation> entry = bfsQ.pollFirstEntry();
            Object attrVal = engine.extractor.getAttr(data, entry.getValue().attr());
            BatchMatcher matcher = entry.getKey();
            Set<Node<RuleLeaf, RuleTrieElement>> matchedRule = matcher.batchMatch(data,attrVal, matchedRulesRepeat);
            matchedRulesRepeat.addAll(matchedRule);
            matchedOp.add(entry.getValue());
            matchedRule.stream().flatMap(node -> node.getData().getChildOperations().stream()).filter(operation -> !matchedOp.contains(operation)).forEach(operation -> {
                BatchMatcher toAdd = engine.batchMatcherMapper.routeMatchers(operation);
                bfsQ.put(toAdd, operation);
            });
        }
        return matchedRulesRepeat;
    }

    public MatchResult match(Object data) {
        MatchResult result = new MatchResult();
        Set<Node<RuleLeaf, RuleTrieElement>> matchedRulesRepeat = innerMatch(data);
        Set<RuleHandler> handlerSet = matchedRulesRepeat.stream()
                .map(matched -> matched.getData()).filter(Objects::nonNull)
                .map(element -> element.getHandlers())
                .filter(Objects::nonNull).flatMap(handlers -> handlers.stream())
                .collect(Collectors.toSet());
        result.setMatchedData(handlerSet);
        return result;
    }

    public RuleTrie debugPut(Rule rule, Engine engine) {
        if (rule instanceof RuleAnd) {
            debugPutAnd((RuleAnd) rule, engine);
        } else if (rule instanceof RuleOr) {
            deBugPutOr((RuleOr) rule, engine);
        } else {
            RuleAnd and = (RuleAnd) RuleAnd.newAnd(rule);
            debugPutAnd(and, engine);
        }
        return this;
    }

    public RuleTrie debugPut(Rule rule, DebugHandler handler) {
        if (rule instanceof RuleAnd) {
            debugPutAnd((RuleAnd) rule, handler);
        } else if (rule instanceof RuleOr) {
            deBugPutOr((RuleOr) rule, handler);
        } else {
            RuleAnd and = (RuleAnd) RuleAnd.newAnd(rule);
            debugPutAnd(and, handler);
        }
        return this;
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
    public RuleTrie deBugPutOr(RuleOr or, Engine engine) {
        or.getOrRuleSet().stream().forEach(r -> debugPut(r, engine));
        return this;
    }
    public RuleTrie deBugPutOr(RuleOr or, DebugHandler selfDefDebugHandler) {
        or.getOrRuleSet().stream().forEach(r -> debugPut(r, selfDefDebugHandler));
        return this;
    }
    private RuleTrie debugPutAnd(RuleAnd and, Engine engine) {
        List<RuleLeaf> ids = and.getAndRuleList().stream().sorted().map(r -> ((RuleLeaf) r)).collect(Collectors.toList());
        ids.stream().filter(RuleLeaf::needDebug)
                .map(ruleLeaf -> Tuple.of(replaceDebugLeaf(ids,ruleLeaf),ruleLeaf.buildDebugHandlers(engine)))
                .forEach(tuple2->{
                    List<DebugHandler> handlers = tuple2._2();
                    List<RuleLeaf> rule = tuple2._1();
                    handlers.stream().forEach(h->putAnd(rule,h));
                });
        return this;
    }

    private RuleTrie debugPutAnd(RuleAnd and,  DebugHandler selfDefDebugHandler) {
        List<RuleLeaf> ids = and.getAndRuleList().stream().sorted().map(r -> ((RuleLeaf) r)).collect(Collectors.toList());
        ids.stream().filter(RuleLeaf::needDebug)
                .map(ruleLeaf -> Tuple.of(replaceDebugLeaf(ids,ruleLeaf),selfDefDebugHandler.deepCopy().setRuleLeaf(ruleLeaf)))
                .forEach(tuple2->{
                    DebugHandler handler = tuple2._2();
                    List<RuleLeaf> rule = tuple2._1();
                    putAnd(rule,handler);
                });
        return this;
    }

    List<RuleLeaf> replaceDebugLeaf(List<RuleLeaf> ids ,RuleLeaf toReplace){
        return ids.stream().map(rl->{
            if (toReplace.equals(rl)){
                return rl.debugReplaceRule();
            }
            return rl;
        }).collect(Collectors.toList());
    }

    public RuleTrie putOr(RuleOr or, RuleHandler handler) {
        or.getOrRuleSet().stream().forEach(r -> putOnly(r, handler));
        return this;
    }
    private RuleTrie putAnd(List<RuleLeaf> and, RuleHandler handler) {
        List<RuleLeaf> ids = and;
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
                matcher.supportedOperation(op);
                matcherNode.setData(matcher);
            }
            matcher.putRule(r, n);
            current = n.getChildren();
        }
        return this;
    }
    private RuleTrie putAnd(RuleAnd and, RuleHandler handler) {
        List<RuleLeaf> ids = and.getAndRuleList().stream().sorted().map(r -> ((RuleLeaf) r)).collect(Collectors.toList());
        return putAnd(ids,handler);
    }


    private Stream<Node<RuleLeaf, RuleTrieElement>>  andGet(RuleAnd and) {
        //Beta 树操作
        List<RuleLeaf> ids = and.getAndRuleList().stream().sorted().map(r -> ((RuleLeaf) r)).collect(Collectors.toList());
        IdentityPath<RuleLeaf> path = IdentityPath.of(ids);
        Node<RuleLeaf, RuleTrieElement> node = getOrCreate(path);
        return Stream.of(node);
    }

}
