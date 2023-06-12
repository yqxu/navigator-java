package com.pingpongx.smb.store;


import com.pingpongx.smb.common.IdentityPath;
import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.common.Trie;
import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.MatchOperation;
import com.pingpongx.smb.rule.routers.operatiors.batch.BatchMatcher;

import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BatchMatcherMapper {
    Trie<String, BatchMatcher> ruleTries = new Trie<>();
    Engine engine;

    public BatchMatcherMapper(Engine engine) {
        this.engine = engine;
    }

    public Node<String, BatchMatcher> matchers(MatchOperation operation) {
        IdentityPath<String> path = IdentityPath.of(Stream.of(operation.obj(), operation.attr(), (String) operation.getIdentify()).collect(Collectors.toList()));
        Node<String, BatchMatcher> node = ruleTries.getOrCreate(path);
        return node;
    }

    public BatchMatcherMapper put(MatchOperation operation, BatchMatcher matcher) {
        matchers(operation).setData(matcher);
        return this;
    }


    public BatchMatcher routeMatchers(MatchOperation operation) {
        return matchers(operation).getData();
    }

}
