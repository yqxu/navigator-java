package com.pingpongx.smb.warning.biz.alert.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

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
@Data
public class GrafanaAlerts {
    private static final long serialVersionUID = -5236469978192983192L;

    List<GrafanaAlert> alerts;
    Map<String,String> commonLabels;
    Map<String,String>  commonAnnotations;

}
