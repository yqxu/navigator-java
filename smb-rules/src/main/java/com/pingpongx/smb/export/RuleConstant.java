package com.pingpongx.smb.export;

import com.pingpongx.smb.rule.routers.operatiors.*;

public class RuleConstant {
    public static enum Operations{
        StrContains(StringContains.class.getSimpleName()),
        StrEquals(StrEquals.class.getSimpleName()),
        StrNotBlank(StrNotBlank.class.getSimpleName()),
        NumEquals(NumEquals.class.getSimpleName()),
        NumberRangeIn(NumRangeIn.class.getSimpleName()),
        ;

        String simpleName;

        public String getSimpleName() {
            return simpleName;
        }

        public void setSimpleName(String simpleName) {
            this.simpleName = simpleName;
        }

        Operations(String simpleName) {
            this.simpleName = simpleName;
        }
    }
}
