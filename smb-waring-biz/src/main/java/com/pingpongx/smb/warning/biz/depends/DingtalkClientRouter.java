package com.pingpongx.smb.warning.biz.depends;

import com.dingtalk.api.DingTalkClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DingtalkClientRouter {
    private Map<String, DingTalkClient> clients;

    public void put(String depart,String url){

    }

    public DingTalkClient get(String depart){
        return clients.get(depart);
    }
}
