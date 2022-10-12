package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.alert.global.GlobalCountContext;
import com.pingpongx.smb.warning.biz.moudle.IdentityPath;
import org.springframework.context.ApplicationContext;

public abstract class AbstractRuleHandler<T> implements RuleHandler<T>{

    private IdentityPath<String> path;
    private ApplicationContext applicationContext;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RuleHandler)){
            return false;
        }
        return getIdentify().equals(((RuleHandler<?>) o).getIdentify());
    }

    @Override
    public int hashCode() {
        return getIdentify().hashCode();
    }

    public IdentityPath<String> getPath(){
        return path;
    }
    public void setPath(IdentityPath<String> path){
        this.path = path;
    }

}
