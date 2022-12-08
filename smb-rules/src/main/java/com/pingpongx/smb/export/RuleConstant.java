package com.pingpongx.smb.export;

import com.pingpongx.smb.rule.routers.operatiors.StrEquals;
import com.pingpongx.smb.rule.routers.operatiors.StringContains;

public class RuleConstant {
    public static class Operations{
        public static String StrContains = StringContains.class.getSimpleName();
        public static String Equals = StrEquals.class.getSimpleName();

        public static String NumBiggerThen = com.pingpongx.smb.rule.routers.operatiors.NumBiggerThen.class.getSimpleName();
    }
}
