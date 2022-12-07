package com.pingpongx.smb.warning.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pingpongx.business.dal.core.BaseDO;
import lombok.Data;

import java.io.Serializable;

/**
 * 客户丢失订单
 *
 * @TableName customer_churn_order
 */
@TableName(value = "customer_churn_order")
@Data
public class CustomerOutflowOrder extends BaseDO implements Serializable {

    private Long orderId;

    /**
     * clientId
     */
    private String clientId;

    /**
     * jira issueId
     */
    private Long jiraId;

    /**
     * salesEmail
     */
    private String salesEmail;

    private static final long serialVersionUID = 1L;
}