package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.Identified;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.MatchOperation;

import java.util.stream.Collectors;

/***
 *
 * @param <D> 在D对象下，生效该Rule规则
 * @param <T> 规则描述了D对象下字段类型T相关的约束描述
 */
public interface Rule<D,T> extends Comparable<Rule<D,T>> {

    default Rule and(Rule rule){
        if (this instanceof RuleAnd){
            ((RuleAnd)this).and(rule);
            return this;
        }else if (this instanceof RuleOr){
            ((RuleOr)this).and(rule);
        }
        return RuleAnd.newAnd(this).and(rule);
    }
    default Rule or(Rule rule){
        if (this instanceof RuleOr){
            return ((RuleOr)this).or(rule);
        }
        return RuleOr.newOr(this).or(rule);
    }
    default RuleOr expansion(){
        if (this instanceof RuleOr){
            return ((RuleOr)this).expansion();
        }else if (this instanceof RuleAnd){
            return ((RuleAnd)this).expansion();
        }
        return RuleOr.newOr(this);
    }

    default int compareTo(Rule<D, T> o) {
        int one,other ;
        if (this instanceof  RuleAnd){
            one = -1;
        }else if (this instanceof  RuleOr){
            one = -2;
        }else {
            one = ((RuleLeaf<D, T>) this).operatorType().sortBy();
        }

        if (o instanceof  RuleAnd){
            other = -1;
        }else if (o instanceof  RuleOr){
            other = -2;
        }else {
            other = ((RuleLeaf<D, T>) this).operatorType().sortBy();
        }
        return one - other;
    }
}
