package com.pingpongx.smb.export;

import com.pingpongx.smb.rule.routers.operatiors.*;

public class RuleConstant {
    public static class Operations{
        public static String StrContains = StringContains.class.getSimpleName();
        public static String StrEquals = StrEquals.class.getSimpleName();
        public static String NumEquals = NumEquals.class.getSimpleName();
        public static String NumBiggerThen = NumBiggerThen.class.getSimpleName();
        public static String NumBiggerAndEqualsThen = NumBiggerAndEqualsThen.class.getSimpleName();
        public static String NumLessThen = NumLessThen.class.getSimpleName();
        public static String NumLessAndEqualsThen = NumLessAndEqualsThen.class.getSimpleName();
    }
}
