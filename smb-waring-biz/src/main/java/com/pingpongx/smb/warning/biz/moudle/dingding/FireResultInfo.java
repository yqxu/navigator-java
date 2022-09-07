package com.pingpongx.smb.warning.biz.moudle.dingding;

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

    private static final long serialVersionUID = -2764938463103742624L;
    private String appName;
    private String className;
    private String content;
    private String message;
    private String hostName;
    private String ip;
    private String level;
    private String traceId;
}
