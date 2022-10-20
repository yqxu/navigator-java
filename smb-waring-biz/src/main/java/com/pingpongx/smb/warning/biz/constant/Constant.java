package com.pingpongx.smb.warning.biz.constant;

import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StrEquals;
import com.pingpongx.smb.warning.biz.alert.routers.operatiors.StringContains;

public class Constant {
    public static class B2B{
        public static String name = "B2B";
        public static String url = "https://oapi.dingtalk.com/robot/send?access_token=db734944ee5a606ba23d5102c5ae0e86eb8e46cc1e9216416a4424410699e9ca";
    }
    public static class  Mid{
        public static String name = "MID";
        public static String url = "https://oapi.dingtalk.com/robot/send?access_token=474919748888ba6871e25f13abb01ff6a9441ae5ae2e51212e0ee48cea67f7c4";
    }
    public static class  FlowMore{
        public static String name = "FLOWMORE";
        public static String url = "https://oapi.dingtalk.com/robot/send?access_token=8408214362f2362da09e9de72732c53306d6716daf95111b32db62ca681a7246";
    }
    public static class Merchant {
        public static String name = "MERCHANT";
        public static String url = "https://oapi.dingtalk.com/robot/send?access_token=7f729e976805ed64ed6ab12d5eef5b9c718eccebd89bc53632c35ab6c625479c";
    }

    public static class Operations{
        public static String StrContains = StringContains.class.getSimpleName();
        public static String Equals = StrEquals.class.getSimpleName();
    }
}
