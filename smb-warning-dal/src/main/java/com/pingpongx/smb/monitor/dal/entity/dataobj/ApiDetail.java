package com.pingpongx.smb.monitor.dal.entity.dataobj;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pingpongx.business.dal.core.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Data
@TableName("ui_monitor_record_api_detail")
public class ApiDetail extends BaseDO implements Serializable {


    private static final long serialVersionUID = -2577919410657049503L;

    @TableField("request_url")
    private String requestUrl;
    private String response;
    @TableField("response_code")
    private String responseCode;
    private String method;
    @TableField("used_time")
    private String usedTime;

}
