package com.pingpongx.smb.warning.api.request;

import com.pingpongx.business.common.exception.Assert;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JiraGenerateRequest implements Serializable {

    private static final long serialVersionUID = -5401591050912937870L;
    /**
     * 追踪id
     */
    private String traceId;
    /**
     * 应用名
     */
    private String appName;
    /**
     * 概要
     */
    private String summary;
    /**
     * 描述
     */
    private String description;

    public void check() {
        Assert.warnNotEmpty(traceId, "Param 'traceId' can't be null");
        Assert.warnNotEmpty(appName, "Param 'appName' can't be null");
        Assert.warnNotEmpty(summary, "Param 'summary' can't be null");
        Assert.warnNotEmpty(description, "Param 'description' can't be null");
    }
}
