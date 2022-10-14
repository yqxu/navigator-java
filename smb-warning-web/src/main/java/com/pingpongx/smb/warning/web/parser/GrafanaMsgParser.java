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

/***
 * {
 * 	"receiver": "",
 * 	"status": "firing",
 * 	"alerts": [{
 * 		"status": "firing",
 * 		"labels": {
 * 			"alertname": "TestAlert",
 * 			"depart": "merchant",
 * 			"instance": "Grafana"
 *                },
 * 		"annotations": {
 * 			"summary": "bbbbb"
 *        },
 * 		"startsAt": "2022-10-12T22:25:58.549376523+08:00",
 * 		"endsAt": "0001-01-01T00:00:00Z",
 * 		"generatorURL": "",
 * 		"fingerprint": "6d25c9d16cf81811",
 * 		"silenceURL": "http://prod-hz-grafana.pingpongx.com/alerting/silence/new?alertmanager=grafana\u0026matcher=alertname%3DTestAlert\u0026matcher=depart%3Dmerchant\u0026matcher=instance%3DGrafana",
 * 		"dashboardURL": "",
 * 		"panelURL": "",
 * 		"valueString": "[ metric='foo' labels={instance=bar} value=10 ]"* 	}],
 * 	"groupLabels": {},
 * 	"commonLabels": {
 * 		"alertname": "TestAlert",
 * 		"depart": "merchant",
 * 		"instance": "Grafana"* 	},
 * 	"commonAnnotations": {
 * 		"summary": "bbbbb"* 	},
 * 	"externalURL": "http://prod-hz-grafana.pingpongx.com/",
 * 	"version": "1",
 * 	"groupKey": "{alertname=\"TestAlert\", depart=\"merchant\", instance=\"Grafana\"}2022-10-12 22:25:58.549376523 +0800 CST m=+4276616.349693638",
 * 	"truncatedAlerts": 0,
 * 	"orgId": 1,
 * 	"title": "[FIRING:1]  (TestAlert merchant Grafana)",
 * 	"state": "alerting",
 * 	"message": "**Firing**\n\nValue: [ metric='foo' labels={instance=bar} value=10 ]\nLabels:\n - alertname = TestAlert\n - depart = merchant\n - instance = Grafana\nAnnotations:\n - summary = bbbbb\nSilence: http://prod-hz-grafana.pingpongx.com/alerting/silence/new?alertmanager=grafana\u0026matcher=alertname%3DTestAlert\u0026matcher=depart%3Dmerchant\u0026matcher=instance%3DGrafana\n"
 * }
 */

@Component
public class GrafanaMsgParser implements AlertParser {

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
        if (data == null){
            return null;
        }
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
        return Stream.of(Constant.Mid.name,Constant.B2B.name,Constant.FlowMore.name,Constant.Merchant.name).collect(Collectors.toSet());
    }


}
