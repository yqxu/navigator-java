package com.pingpongx.smb.warning.web.parser;

import com.alibaba.fastjson.JSON;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import com.pingpongx.smb.warning.biz.alert.model.GrafanaAlert;
import com.pingpongx.smb.warning.biz.alert.model.GrafanaAlerts;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class GrafanaMsgParser implements AlertParser {

    @Override
    public List<ThirdPartAlert> toAlerts(String data) {
        if (data == null){
            return null;
        }
        GrafanaAlerts alerts = JSON.parseObject(data, GrafanaAlerts.class);
        return alerts.getAlerts().stream().collect(Collectors.toList());
    }

    @Override
    public String toDingTalkMsg(ThirdPartAlert data) {
        return data.throwContent();
    }

    @Override
    public JiraGenerateRequest generateJiraRequest(ThirdPartAlert alert) {
        GrafanaAlert fireResultInfo = (GrafanaAlert) alert;
        String summary = StringUtils.substring(fireResultInfo.throwContent(), 0, 200);
        JiraGenerateRequest build = JiraGenerateRequest.builder()
                .appName(fireResultInfo.throwAppName())
                .traceId("#")
                .summary(summary)
                .description(fireResultInfo.throwContent())
                .build();
        return build;
    }

    @Override
    public Set<String> getSupportDepart() {
        return Stream.of(Constant.Merchant.name,Constant.B2B.name,Constant.FlowMore.name,Constant.Mid.name,Constant.TEST.name).map(name->"Grafana-"+name).collect(Collectors.toSet());
    }


}
