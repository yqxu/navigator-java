package com.pingpongx.smb.warning.biz.alert.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/***
 * {
 * 	"action_policy_url": "https://sls.console.aliyun.com/lognext/project/pp-merchant-app-log/alertcenter?recordId=m_ap_1663739539565&tab=action_policy",
 * 	"alert_history_dashboard_url": "https://sls.console.aliyun.com/lognext/project/pp-merchant-app-log/dashboard/internal-alert-analysis",
 * 	"alert_id": "alert-1586773930-430004",
 * 	"alert_instance_id": "99783f2a4b1a6b81-5eaac0c68be34-2b916d3",
 * 	"alert_name": "应用日志异常报警",
 * 	"alert_policy_url": "https://sls.console.aliyun.com/lognext/project/pp-merchant-app-log/alertcenter?recordId=sls.builtin.dynamic&tab=alert_policy",
 * 	"alert_time": 1665399547,
 * 	"alert_type": "sls_alert",
 * 	"alert_url": "https://sls.console.aliyun.com/lognext/project/pp-merchant-app-log/alert/alert-1586773930-430004",
 * 	"aliuid": "1915657960764041",
 * 	"annotations": {
 * 		"desc": "应用日志异常报警",
 * 		"title": "应用日志异常报警"
 *        },
 * 	"condition": "Count:[3] > 0; Condition:[1] > 0",
 * 	"dashboard": "dashboard-1586773930-450870",
 * 	"dashboard_url": "https://sls.console.aliyun.com/lognext/project/pp-merchant-app-log/dashboard/dashboard-1586773930-450870",
 * 	"fingerprint": "2eef5065da51c9c0",
 * 	"fire_results": [{
 * 		"application": "promotion-manager",
 * 		"content": "[2022-10-10 18:58:46,849] [WARN] [AbstractApplicationContext:551] [localhost-startStop-1] -- [TID: N/A]   Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dingDingAuditTask': Injection of @DubboReference dependencies is failed; nested exception is java.lang.IllegalStateException: Failed to check the status of the service com.pingpongx.operation.data.service.OperationDataApi. No provider available for the service com.pingpongx.operation.data.service.OperationDataApi:1.0.0 from the url dubbo://192.168.100.24/com.pingpongx.operation.data.service.OperationDataApi?anyhost=true&application=promotion-manager&bean.name=ServiceBean:com.pingpongx.operation.data.service.OperationDataApi:1.0.0&check=false&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&init=false&interface=com.pingpongx.operation.data.service.OperationDataApi&metadata-type=remote&methods=getWithdrawFee,listClientInfo,getDingPro",
 * 		"total": "1"
 *    }, {
 * 		"application": "promotion-manager",
 * 		"content": "[2022-10-10 18:58:46,895] [ERROR] [SpringApplication:771] [localhost-startStop-1] -- [TID: N/A]   Application startup failed\norg.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dingDingAuditTask': Injection of @DubboReference dependencies is failed; nested exception is java.lang.IllegalStateException: Failed to check the status of the service com.pingpongx.operation.data.service.OperationDataApi. No provider available for the service com.pingpongx.operation.data.service.OperationDataApi:1.0.0 from the url dubbo://192.168.100.24/com.pingpongx.operation.data.service.OperationDataApi?anyhost=true&application=promotion-manager&bean.name=ServiceBean:com.pingpongx.operation.data.service.OperationDataApi:1.0.0&check=false&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&init=false&interface=com.pingpongx.operation.data.service.OperationDataApi&metadata-type=remote&methods=getWithdrawFee,listClientInfo,getDingProcessDetail,pageClientInfo,adminChannel,getInboundAmount,countSt",
 * 		"total": "1"
 *    }, {
 * 		"application": "promotion-manager",
 * 		"content": "10-Oct-2022 18:58:46.899 SEVERE [Catalina-startStop-1] org.apache.catalina.core.ContainerBase.startInternal A child container failed during start\n java.util.concurrent.ExecutionException: org.apache.catalina.LifecycleException: Failed to start component [StandardEngine[Catalina].StandardHost[localhost].StandardContext[]]\n\tat java.util.concurrent.FutureTask.report(FutureTask.java:122)\n\tat java.util.concurrent.FutureTask.get(FutureTask.java:192)\n\tat org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:942)\n\tat org.apache.catalina.core.StandardHost.startInternal(StandardHost.java:872)\n\tat org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)\n\tat org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1423)\n\tat org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1413)\n\tat java.util.concurrent.FutureTask.run(FutureTask.java:266)\n\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\n\tat java.util.concurrent.Threa",
 * 		"total": "1"
 *    }],
 * 	"fire_results_count": 3,
 * 	"fire_time": 1665399547,
 * 	"incident_url": "https://sls.console.aliyun.com/lognext/project/pp-merchant-app-log/alertcenter?incidentid=d26df0a7a08b7637346b0d64f90ddac783b8b45a4c5b4ac4deb2bda526d8d2d8&tab=alert_center&viewtype=incident",
 * 	"labels": {},
 * 	"next_eval_interval": 300,
 * 	"policy": {
 * 		"alert_policy_id": "sls.builtin.dynamic",
 * 		"action_policy_id": "m_ap_1663739539565",
 * 		"use_default": false,
 * 		"repeat_interval": "5m0s"
 *    },
 * 	"project": "pp-merchant-app-log",
 * 	"query_url": "https://sls.console.aliyun.com/lognext/project/pp-merchant-app-log/logsearch/merchant?encode=base64&endTime=1665399547&queryString=KCJlcnJvciIgb3IgIkVSUk9SIikgYW5kICgiZXhjZXB0aW9uIiBvciAiRVhDRVBUSU9OIiBvciAi5pyN5Yqh5Zmo5YaF6YOo6ZSZ6K%2BvIikgfHNlbGVjdCBhcHBsaWNhdGlvbiwxIGFzIHRvdGFsLGNvbnRlbnQ%3D&queryTimeType=99&startTime=1665399247",
 * 	"raw_condition": "Count:__count__ > 0; Condition:total>0",
 * 	"region": "cn-hangzhou",
 * 	"resolve_time": 0,
 * 	"results": [{
 * 		"dashboard_url": "https://sls.console.aliyun.com/lognext/project/pp-merchant-app-log/dashboard/dashboard-1586773930-450870",
 * 		"end_time": 1665399547,
 * 		"fire_result": {
 * 			"application": "promotion-manager",
 * 			"content": "[2022-10-10 18:58:46,849] [WARN] [AbstractApplicationContext:551] [localhost-startStop-1] -- [TID: N/A]   Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dingDingAuditTask': Injection of @DubboReference dependencies is failed; nested exception is java.lang.IllegalStateException: Failed to check the status of the service com.pingpongx.operation.data.service.OperationDataApi. No provider available for the service com.pingpongx.operation.data.service.OperationDataApi:1.0.0 from the url dubbo://192.168.100.24/com.pingpongx.operation.data.service.OperationDataApi?anyhost=true&application=promotion-manager&bean.name=ServiceBean:com.pingpongx.operation.data.service.OperationDataApi:1.0.0&check=false&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&init=false&interface=com.pingpongx.operation.data.service.OperationDataApi&metadata-type=remote&methods=getWithdrawFee,listClientInfo,getDingPro",
 * 			"total": "1"
 *        },
 * 		"project": "pp-merchant-app-log",
 * 		"query": "(\"error\" or \"ERROR\") and (\"exception\" or \"EXCEPTION\" or \"服务器内部错误\") |select application,1 as total,content",
 * 		"query_url": "https://sls.console.aliyun.com/lognext/project/pp-merchant-app-log/logsearch/merchant?encode=base64&endTime=1665399547&queryString=KCJlcnJvciIgb3IgIkVSUk9SIikgYW5kICgiZXhjZXB0aW9uIiBvciAiRVhDRVBUSU9OIiBvciAi5pyN5Yqh5Zmo5YaF6YOo6ZSZ6K%2BvIikgfHNlbGVjdCBhcHBsaWNhdGlvbiwxIGFzIHRvdGFsLGNvbnRlbnQ%3D&queryTimeType=99&startTime=1665399247",
 * 		"raw_results": [{
 * 			"application": "promotion-manager",
 * 			"content": "[2022-10-10 18:58:46,849] [WARN] [AbstractApplicationContext:551] [localhost-startStop-1] -- [TID: N/A]   Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dingDingAuditTask': Injection of @DubboReference dependencies is failed; nested exception is java.lang.IllegalStateException: Failed to check the status of the service com.pingpongx.operation.data.service.OperationDataApi. No provider available for the service com.pingpongx.operation.data.service.OperationDataApi:1.0.0 from the url dubbo://192.168.100.24/com.pingpongx.operation.data.service.OperationDataApi?anyhost=true&application=promotion-manager&bean.name=ServiceBean:com.pingpongx.operation.data.service.OperationDataApi:1.0.0&check=false&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&init=false&interface=com.pingpongx.operation.data.service.OperationDataApi&metadata-type=remote&methods=getWithdrawFee,listClientInfo,getDingPro",
 * 			"total": "1"
 *        }, {
 * 			"application": "promotion-manager",
 * 			"content": "[2022-10-10 18:58:46,895] [ERROR] [SpringApplication:771] [localhost-startStop-1] -- [TID: N/A]   Application startup failed\norg.springframework.beans.factory.BeanCreationException: Error creating bean with name 'dingDingAuditTask': Injection of @DubboReference dependencies is failed; nested exception is java.lang.IllegalStateException: Failed to check the status of the service com.pingpongx.operation.data.service.OperationDataApi. No provider available for the service com.pingpongx.operation.data.service.OperationDataApi:1.0.0 from the url dubbo://192.168.100.24/com.pingpongx.operation.data.service.OperationDataApi?anyhost=true&application=promotion-manager&bean.name=ServiceBean:com.pingpongx.operation.data.service.OperationDataApi:1.0.0&check=false&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&init=false&interface=com.pingpongx.operation.data.service.OperationDataApi&metadata-type=remote&methods=getWithdrawFee,listClientInfo,getDingProcessDetail,pageClientInfo,adminChannel,getInboundAmount,countSt",
 * 			"total": "1"
 *        }, {
 * 			"application": "promotion-manager",
 * 			"content": "10-Oct-2022 18:58:46.899 SEVERE [Catalina-startStop-1] org.apache.catalina.core.ContainerBase.startInternal A child container failed during start\n java.util.concurrent.ExecutionException: org.apache.catalina.LifecycleException: Failed to start component [StandardEngine[Catalina].StandardHost[localhost].StandardContext[]]\n\tat java.util.concurrent.FutureTask.report(FutureTask.java:122)\n\tat java.util.concurrent.FutureTask.get(FutureTask.java:192)\n\tat org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:942)\n\tat org.apache.catalina.core.StandardHost.startInternal(StandardHost.java:872)\n\tat org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)\n\tat org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1423)\n\tat org.apache.catalina.core.ContainerBase$StartChild.call(ContainerBase.java:1413)\n\tat java.util.concurrent.FutureTask.run(FutureTask.java:266)\n\tat java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)\n\tat java.util.concurrent.Threa",
 * 			"total": "1"
 *        }],
 * 		"raw_results_count": 3,
 * 		"region": "cn-hangzhou",
 * 		"role_arn": "",
 * 		"start_time": 1665399247,
 * 		"store": "merchant",
 * 		"store_type": "log"
 *    }],
 * 	"severity": 6,
 * 	"signin_url": "https://sls.console.aliyun.com/console/AlertAjax/slsSignIn.json?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NjYwMDQzNDgsInVpZCI6ImU1MzIyNWU3Y2I5YjBhNWRlNzJiYmQ5ZjQwODc4MWEwMWY4ZWIyMjkxZmRkOGM2YmI5MjIzZDFhMTg4NjNiNmNlNGEwNzQ4OWI2MGE4NzliMGQ5MGE5YzQiLCJ1cmwiOiJodHRwczovL3Nscy5jb25zb2xlLmFsaXl1bi5jb20vbG9nbW9iaWxlL3Byb2plY3QvcHAtbWVyY2hhbnQtYXBwLWxvZy9pbmNpZGVudC9kMjZkZjBhN2EwOGI3NjM3MzQ2YjBkNjRmOTBkZGFjNzgzYjhiNDVhNGM1YjRhYzRkZWIyYmRhNTI2ZDhkMmQ4P2luc3RhbmNlPTk5NzgzZjJhNGIxYTZiODEtNWVhYWMwYzY4YmUzNC0yYjkxNmQzXHUwMDI2bG9jYWxlPXpoLUNOIn0.I7hO_z9uJLlHQ3nXvmyNjfDotn6b9gDRPkoFSz3-BXE",
 * 	"status": "firing"
 * }
 */
@Data
public class MerchantAlerts {
    private static final long serialVersionUID = -5136469378192983192L;
    List<MerchantAlert> fire_results;
    Map<String,String> annotations;
}
