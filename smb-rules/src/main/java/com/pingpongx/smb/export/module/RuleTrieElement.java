package com.pingpongx.smb.export.module;

import com.pingpongx.smb.export.spi.RuleHandler;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class RuleTrieElement {
    private static volatile long currentIndex = 0l;
    Long id;
    TreeSet<MatchOperation> childOperations = new TreeSet<>();
    Set<RuleHandler> handlers = new HashSet<>();

    public static RuleTrieElement build() {
        RuleTrieElement ret = new RuleTrieElement();
        ret.id = currentIndex++;
        return ret;
    }

    public static long getCurrentIndex() {
        return currentIndex;
    }

    public static void setCurrentIndex(long currentIndex) {
        RuleTrieElement.currentIndex = currentIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TreeSet<MatchOperation> getChildOperations() {
        return childOperations;
    }

    public void setChildOperations(TreeSet<MatchOperation> childOperations) {
        this.childOperations = childOperations;
    }

    public Set<RuleHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(Set<RuleHandler> handlers) {
        this.handlers = handlers;
    }

    public boolean removeHandler(String handlerIdentify){
        return this.handlers.removeIf(ruleHandler -> ruleHandler.getIdentify().equals(handlerIdentify));
    }

    public boolean removeHandler(RuleHandler handler){
        return this.handlers.remove(handler);
    }

}
