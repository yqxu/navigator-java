package com.pingpongx.smb.warning.web.parser;

import com.alibaba.fastjson.JSON;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
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
public class B2BParser implements AlertParser {
    @Override
    public ThirdPartAlert toAlert(String data) {
        if (data == null){
            return null;
        }
        SlsAlert ret = JSON.parseObject(data, SlsAlert.class);
        return ret;
    }

    @Override
    public List<ThirdPartAlert> toAlerts(String data) {
        List<ThirdPartAlert> ret = JSON.parseArray(data, SlsAlert.class).stream().collect(Collectors.toList());
        return ret;
    }

    @Override
    public String toDingTalkMsg(ThirdPartAlert data) {
        return data.throwContent();
    }

    @Override
    public JiraGenerateRequest generateJiraRequest(ThirdPartAlert alert) {
        SlsAlert fireResultInfo = (SlsAlert) alert;
        String summary = StringUtils.substring(fireResultInfo.getContent(), 0, 200);
        JiraGenerateRequest build = JiraGenerateRequest.builder()
                .appName(fireResultInfo.getAppName())
                .traceId(StringUtils.defaultIfBlank(fireResultInfo.getTraceId(),"#"))
                .summary(summary)
                .description(fireResultInfo.getContent())
                .build();
        return build;
    }

    @Override
    public Set<String> getSupportDepart() {
        return Stream.of(Constant.Mid.name,Constant.B2B.name,Constant.FlowMore.name).collect(Collectors.toSet());
    }


}
