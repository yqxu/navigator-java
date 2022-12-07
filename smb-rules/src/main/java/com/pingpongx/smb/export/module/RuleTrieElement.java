package com.pingpongx.smb.export.module;

import com.pingpongx.smb.common.IdentityPath;
import com.pingpongx.smb.common.Node;
import com.pingpongx.smb.common.Trie;
import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.operation.RuleAnd;
import com.pingpongx.smb.export.module.operation.RuleLeaf;
import com.pingpongx.smb.export.module.operation.RuleOr;
import com.pingpongx.smb.export.spi.RuleHandler;
import com.pingpongx.smb.rule.routers.operatiors.batch.BatchMatcher;
import com.pingpongx.smb.rule.routers.operatiors.batch.BatchMatcherFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RuleTrieElement {
   private static volatile long currentIndex=0l;

   public static RuleTrieElement build(){
      RuleTrieElement ret = new RuleTrieElement();
      ret.id = currentIndex++;
      return ret;
   }

   Long id;
   TreeSet<MatchOperation> childOperations = new TreeSet<>();
   Set<RuleHandler> handlers = new HashSet<>();

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

}
