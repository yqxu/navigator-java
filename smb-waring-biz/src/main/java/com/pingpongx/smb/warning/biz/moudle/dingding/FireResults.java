package com.pingpongx.smb.warning.biz.moudle.dingding;

import java.io.Serializable;
import lombok.Data;

/**
 * @Auther: jiangkun
 * @Date: 2022/06/22/10:35 上午
 * @Description:
 * @Version:
 */
@Data
public class FireResults implements Serializable {

    private static final long serialVersionUID = -3791152964348125544L;
    private String _container_name_;
    private String _container_ip_;
    private String traceid;
    private String appName;
    private String class_name;
    private String content;
    private String hostName;
    private String ip;
    private String level;
    private String trace_id;
}
