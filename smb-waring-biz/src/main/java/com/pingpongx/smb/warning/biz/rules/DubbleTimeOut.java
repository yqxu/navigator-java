package com.pingpongx.smb.warning.biz.rules;

import com.pingpongx.smb.warning.biz.moudle.dingding.FireResults;

public class DubbleTimeOut implements Rule<FireResults>{

    /**
     *[Message]:告警内容: [DUBBO] Fail to connect to HeaderExchangeClient [channel=org.apache.dubbo.remoting.transport.netty4.NettyClient
     *
     *
     *
     * 告警内容: ----|50002|System error, please try again later|GET:/api/front/public/map/location|
     * java.lang.RuntimeException: org.apache.dubbo.rpc.RpcException: No provider available from registry b2b-cn-zk1.pingpongx.com:2181 for service com.pingpongx.business.ob.bpm.MapPublicVarsService:1.0 on consumer 192.168.138.113 use dubbo version 2.7.1, please check status of providers(disabled, not registered or in blacklist). at com.pingpongx.business.gateway.web.service.impl.GenericServiceImpl.routeInvoke(GenericServiceImpl.java:176) at
     */
    private static String except="[DUBBO] Fail to connect to HeaderExchangeClient [channel=org.apache.dubbo.remoting.transport.netty4.NettyClient";
    private static String except1 = "No provider available from";
    private static String except2 = "Invoke remote method timeout. method: ";
    @Override
    public boolean contentMatch(FireResults content) {
        if (content==null||content.getContent() == null){
            return false;
        }
        if (content.getContent().contains(except)||content.getContent().contains(except1)||content.getContent().contains(except2)){
            return true;
        }
        return false;
    }
}
