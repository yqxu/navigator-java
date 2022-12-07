package com.pingpongx.smb.warning.web.module;

import lombok.Getter;
import lombok.Setter;

/***
 * 每个销售/运营 流失客户数
 * */
@Setter
@Getter
public class CustomerInfo {

    private String clientid;

    private String clientLostStatus;

    private String clientActiveStatus;

    private String clientActiveWorthStatus;

    private String clientLostWorthStatus;

    private String clientPriorityLevel;

    private int ifHighRisk;

    private String salesEmail;

    private String kaType;

}
