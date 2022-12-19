package com.pingpongx.smb.warning.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pingpongx.business.dal.core.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * jira信息表
 *
 * @TableName jira_info
 */
@TableName(value = "jira_info")
@Data
public class JiraInfo extends BaseDO implements Serializable {

    private Long jiraId;
    /**
     * issueId
     */
    private String issueId;

    /**
     * projectKey
     */
    private String projectKey;

    /**
     * assignee
     */
    private String assignee;

    /**
     * 优先级
     */
    private String priorityLevel;

    /**
     * summary
     */
    private String summary;

    /**
     * issueType
     */
    private String issueType;

    /**
     * url
     */
    private String url;

    /**
     * status
     */
    private String status;

    private Date doneTime;

    private static final long serialVersionUID = 1L;

    public enum PriorityLevel {
        P0, P1, P2
    }

}