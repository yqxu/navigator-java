package com.pingpongx.smb.warning.web.module;

import java.io.Serializable;
import lombok.Data;

/**
 * @Auther: jiangkun
 * @Date: 2022/07/21/10:54 上午
 * @Description:
 * @Version:
 */
@Data
public class FireResultInfo implements Serializable {


    private static final long serialVersionUID = -4211350046801048808L;
    private String appName;
    private String className;
    private String content;
    private String message;
    private String hostName;
    private String ip;
    private String level;
    private String traceId;
}
