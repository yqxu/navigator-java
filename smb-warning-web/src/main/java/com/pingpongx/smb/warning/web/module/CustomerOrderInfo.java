package com.pingpongx.smb.warning.web.module;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerOrderInfo {

    /**
     * clientId
     */
    private String clientId;

    /**
     * jira issueId
     */
    private String issueId;

    /**
     * salesEmail
     */
    private String salesEmail;

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

    private String orderId;

}
