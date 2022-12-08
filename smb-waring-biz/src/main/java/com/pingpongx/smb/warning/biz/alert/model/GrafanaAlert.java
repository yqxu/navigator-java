package com.pingpongx.smb.warning.biz.alert.model;

import com.pingpongx.smb.common.IdentityPath;
import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/***
 * [{
 *  * 		"status": "firing",
 *  * 		"labels": {
 *  * 			"alertname": "TestAlert",
 *  * 			"depart": "merchant",
 *  * 			"instance": "Grafana"
 *  *                },
 *  * 		"annotations": {
 *  * 			"summary": "bbbbb"
 *  *        },
 *  * 		"startsAt": "2022-10-12T22:25:58.549376523+08:00",
 *  * 		"endsAt": "0001-01-01T00:00:00Z",
 *  * 		"generatorURL": "",
 *  * 		"fingerprint": "6d25c9d16cf81811",
 *  * 		"silenceURL": "http://prod-hz-grafana.pingpongx.com/alerting/silence/new?alertmanager=grafana\u0026matcher=alertname%3DTestAlert\u0026matcher=depart%3Dmerchant\u0026matcher=instance%3DGrafana",
 *  * 		"dashboardURL": "",
 *  * 		"panelURL": "",
 *  * 		"valueString": "[ metric='foo' labels={instance=bar} value=10 ]"* 	}]
 */
@Data
public class GrafanaAlert implements ThirdPartAlert{
    private static final long serialVersionUID = -5236469978192983192L;

    Map<String,String> labels;
    Map<String,String> annotations;
    String startsAt;
    String silenceURL;
    String level;

    @Override
    public IdentityPath<String> countPath() {
        return IdentityPath.of(Stream.of(throwAppName()).collect(Collectors.toList()));
    }

    @Override
    public String throwAppName() {
        return depart();
    }

    @Override
    public String throwContent() {
        return "Grafana业务预警：\n"+annotations.get("summary");
    }

    @Override
    public String depart() {
        return "Grafana-"+labels.get("depart").toUpperCase();
    }

    @Override
    public void departSet(String depart) {
    }


}
