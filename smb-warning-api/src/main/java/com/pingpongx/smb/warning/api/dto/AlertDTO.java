package com.pingpongx.smb.warning.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO implements Serializable {
    private static final long serialVersionUID = 6184864350584218351L;
    private String title;
    private String appName;
    private String className;
    private String content;
    private String message;
    private String hostName;
    private String ip;
    private String level;
    private String traceId;
}
