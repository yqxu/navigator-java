package com.pingpongx.smb.warning.biz.depends;

import com.dingtalk.api.DefaultDingTalkClient;
import com.pingpongx.smb.warning.biz.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:47 上午
 * @Description:
 * @Version:
 */
@Slf4j
public class ConfiguredDingTalkClient extends AbstractPPDingTalkClient implements PPDingTalkClient {
    private String depart;

    private String appName;
    public String getNotifyUrl(){
        return notifyUrl;
    }

    public ConfiguredDingTalkClient(String notifyUrl){
        this.notifyUrl = notifyUrl;
        this.dingTalkClient = new DefaultDingTalkClient(getNotifyUrl());
    }
    @Override
    public List<String> supportDepartNames() {
        return Stream.of(depart).collect(Collectors.toList());
    }

    @Override
    public List<String> getAppNames() {
        return Stream.of(appName).collect(Collectors.toList());
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}

