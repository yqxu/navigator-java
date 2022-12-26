package com.pingpongx.smb.warning.web.module;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/***
 * 每个销售/运营 流失客户数
 * */
@Setter
@Getter
public class CustomerInfo {

    @JsonProperty("clientid")
    private String clientId;

    @JsonProperty("client_lost_status")
    private String clientLostStatus;

    @JsonProperty("client_active_status")
    private String clientActiveStatus;

    @JsonProperty("client_active_worth_status")
    private String clientActiveWorthStatus;

    @JsonProperty("client_lost_worth_status")
    private String clientLostWorthStatus;

    @JsonProperty("client_priority_level")
    private String clientPriorityLevel;

    @JsonProperty("if_high_risk")
    private Integer ifHighRisk;

    @JsonProperty("sales_email")
    private String salesEmail;

    @JsonProperty("sub_ka")
    private String kaType;

    private String category;

    @JsonProperty("avg_inbound_cnt_3sm")
    private BigDecimal avgInboundCnt3sm;

}
