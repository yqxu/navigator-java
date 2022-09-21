package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.moudle.dingding.FireResults;

public class BizExceptionRule implements Rule<FireResults>{

    private static String except = "com.pingpongx.business.common.exception.BizException: 邮箱已被注册！";
    @Override
    public boolean contentMatch(FireResults content) {
        if (content==null||content.getContent() == null){
            return false;
        }
        if (content.getContent().contains(except)){
            return true;
        }
        return false;
    }
}
