package com.pingpongx.smb.monitor.biz.job;

import com.alibaba.fastjson.JSON;
import com.pingpongx.job.core.handler.annotation.JobHandler;
import com.pingpongx.smb.monitor.dal.entity.uiprops.B2BHKLoginParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Slf4j
@Component
@JobHandler
public class B2BMonitorHK extends MonitorTemplateJob {

    @Resource
    private B2BHKLoginParam b2bHKLoginParam;

    @Override
    public void initEnv() {
        super.setHost(b2bHKLoginParam.getMonitorB2BHKHost());
        super.setDingGroup("B2BHK");
        super.setBusiness(this.getClass().getSimpleName());
    }

    @Override
    public void login() {
        log.info("B2BHKMonitor 参数信息：{}", JSON.toJSONString(b2bHKLoginParam));

    }

    @Override
    public void actions() {

    }

    @Override
    public void logout() {

    }
}
