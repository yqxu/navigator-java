package com.pingpongx.smb.warning.api.service;

import com.pingpongx.smb.warning.api.dto.AlertUserDTO;
import com.pingpongx.smb.warning.api.dto.DingDingReceiverDTO;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import java.util.List;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:52 下午
 * @Description: 应用告警服务
 * @Version:
 */
public interface BusinessAlertService {

    /**
     * 服务版本号
     */
    String VERSION = "1.0";

    /**
     * 根据应用名查询需要通知的应用负责人
     * @param appName
     * @return
     */
    DingDingReceiverDTO findDingDingReceivers(String appName);


    /**
     * 生成jira
     */
    void generateJira(JiraGenerateRequest request);

    /**
     * 查询所有应用负责人
     * @return 应用负责人数据
     */
    List<AlertUserDTO> getAllPrincipal();

    /**
     * 清除告警应用缓存数据
     */
    void clearCache();

    /**
     * 清除告警应用缓存数据
     * @param appNames 应用名
     */
    void clearCache(List<String> appNames);
}
