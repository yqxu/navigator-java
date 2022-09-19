package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.moudle.dingding.FireResultInfo;

public class DubbleTimeOut implements Rule<FireResultInfo>{

    /**
     *[Message]:告警内容: [DUBBO] Fail to connect to HeaderExchangeClient [channel=org.apache.dubbo.remoting.transport.netty4.NettyClient
     *
     *
     *
     * 告警内容: ----|50002|System error, please try again later|GET:/api/front/public/map/location|
     * java.lang.RuntimeException: org.apache.dubbo.rpc.RpcException: No provider available from registry b2b-cn-zk1.pingpongx.com:2181 for service com.pingpongx.business.ob.bpm.MapPublicVarsService:1.0 on consumer 192.168.138.113 use dubbo version 2.7.1, please check status of providers(disabled, not registered or in blacklist). at com.pingpongx.business.gateway.web.service.impl.GenericServiceImpl.routeInvoke(GenericServiceImpl.java:176) at
     */
    private static String except="[DUBBO] Fail to connect to HeaderExchangeClient [channel=org.apache.dubbo.remoting.transport.netty4.NettyClient";
    private static String dubboTimeOut = "No provider available from";
    @Override
    public boolean contentMatch(FireResultInfo content) {
        if (content==null||content.getMessage() == null){
            return false;
        }
        if (content.getMessage().contains(except)||content.getMessage().contains(dubboTimeOut)){
            return true;
        }
        return false;
    }
}
