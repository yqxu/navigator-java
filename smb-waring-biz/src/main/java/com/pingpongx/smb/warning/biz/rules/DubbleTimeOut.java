package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.moudle.dingding.FireResultInfo;

public class DubbleTimeOut implements Rule<FireResultInfo>{

    @Override
    public boolean contentMatch(FireResultInfo content) {
        if (content.getMessage() == null){
            return false;
        }
//        content.getMessage().startsWith()
//        content.getMessage()
        return false;
    }
}
