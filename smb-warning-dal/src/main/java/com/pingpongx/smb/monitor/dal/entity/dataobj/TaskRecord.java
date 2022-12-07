package com.pingpongx.smb.monitor.dal.entity.dataobj;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pingpongx.business.dal.core.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * todo 增加原因的hashcode值，以便统计最近3次 原因都一样的失败数据？
 */

@EqualsAndHashCode(callSuper = false)
@Data
@TableName("ui_monitor_record")
public class TaskRecord extends BaseDO implements Serializable {

    private static final long serialVersionUID = -7121687309204989610L;

    @TableField("business_line")
    private String businessLine;
    private String environment;
    @TableField("task_result")
    private String taskResult;
    @TableField("fail_reason")
    private String failReason;


}
