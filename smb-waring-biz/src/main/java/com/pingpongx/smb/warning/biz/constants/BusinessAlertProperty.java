package com.pingpongx.smb.warning.biz.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: jiangkun
 * @Date: 2022/08/26/5:18 下午
 * @Description:
 * @Version:
 */
@Configuration
@Data
public class BusinessAlertProperty {

    @Value("${timeOutAlertTimes:2,5}")
    private String[] timeOutAlertTimeInterval;

    @Value("${notify_jira_change_dingTalk_url:https://oapi.dingtalk.com/robot/send?access_token=12481948130fb566f30222d87c5489ae1ade4fb3552336714574d33ee00905a9}")
    private String notify_jira_change_dingTalk_url;

    /**
     * 超时告警钉钉通知地址
     */
    @Value("${notify_time_out_dingTalk_url:https://oapi.dingtalk.com/robot/send?access_token=55a512ac54fcc833cca38fd46d405fd224b6ca8f66d6f8acc1c523555ac6914f}")
    private String notify_time_out_dingTalk_url;
}
