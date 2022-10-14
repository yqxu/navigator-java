package com.pingpongx.smb.warning.web.parser;

import com.alibaba.fastjson.JSON;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import com.pingpongx.smb.warning.biz.alert.model.MerchantAlerts;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MerchantParser implements AlertParser {

    @Override
    public List<ThirdPartAlert> toAlerts(String data) {
        if (data == null){
            return null;
        }
        MerchantAlerts alerts = JSON.parseObject(data, MerchantAlerts.class);
        if (alerts.getFire_results()==null){
            return null;
        }
        List<ThirdPartAlert> ret = alerts.getFire_results().stream().collect(Collectors.toList());
        return ret;
    }

    @Override
    public String toDingTalkMsg(ThirdPartAlert data) {
        return data.throwContent();
    }

    @Override
    public Set<String> getSupportDepart() {
        return Stream.of(Constant.Merchant.name).collect(Collectors.toSet());
    }

    @Override
    public JiraGenerateRequest generateJiraRequest(ThirdPartAlert alert) {
        MerchantAlert fireResultInfo = (MerchantAlert) alert;
        String summary = StringUtils.substring(fireResultInfo.throwContent(), 0, 200);
        JiraGenerateRequest build = JiraGenerateRequest.builder()
                .appName(fireResultInfo.throwAppName())
                .traceId("#")
                .summary(summary)
                .description(fireResultInfo.throwContent())
                .build();
        return build;
    }
}
