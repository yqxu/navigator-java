package com.pingpongx.smb.export.module;

import com.pingpongx.smb.export.module.operation.RuleAnd;
import com.pingpongx.smb.export.module.operation.RuleOr;

import java.lang.reflect.Type;

/***
 *
 * @param <D> 在D对象下，生效该Rule规则
 * @param <T> 规则描述了D对象下字段类型T相关的约束描述
 */
public interface Rule<T> extends Comparable<Rule<T>> {

    String type();
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


}
