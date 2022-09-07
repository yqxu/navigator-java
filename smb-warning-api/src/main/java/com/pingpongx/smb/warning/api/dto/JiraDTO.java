package com.pingpongx.smb.warning.api.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JiraDTO implements Serializable {
    private static final long serialVersionUID = 6184864350584218350L;
    /**
     * jira 地址
     */
    private String url;
    /**
     * jira 状态
     */
    private String status;
    /**
     * 问题来源
     */
    private String issueType;
    /**
     * jira 概要
     */
    private String summary;
    /**
     * 报告人
     */
    private String reporter;
    /**
     * 经办人
     */
    private String assignee;
    /**
     * 处理人
     */
    private String worker;
    /**
     * 问题原因
     */
    private String reason;
    /**
     * 解决方案
     */
    private String action;
}
