package com.pingpongx.smb.warning.web.module;

import com.google.gson.annotations.SerializedName;
import com.pingpongx.smb.cloud.model.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerWarnJira extends Field {
    //任务id
    @SerializedName("customfield_11227")
    private String orderId;

    //客户id
    @SerializedName("customfield_11228")
    private String clientId;

    //客户跟进优先级
    @SerializedName("customfield_11229")
    private Value level;

    //客户分类
    @SerializedName("customfield_11230")
    private Value kaType;

    //客户主营行业一级二级
    @SerializedName("customfield_11232")
    private String IndustryLevel;

    //客户流失标签
    @SerializedName("customfield_11235")
    private Value outflowStatus;

    //客户当前活跃状态
    @SerializedName("customfield_11236")
    private Value activeStatus;

    //客户活跃价值标签
    @SerializedName("customfield_11237")
    private Value activeTag;

    //客户流失价值标签
    @SerializedName("customfield_11238")
    private Value outflowTag;

    //客户交易月均笔数
    @SerializedName("customfield_11234")
    private Integer avgTradeNum;

    //任务跟进人是必需的
    @SerializedName("customfield_11246")
    private Name followUp;

}
