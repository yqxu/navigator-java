package com.pingpongx.smb.warning.web.client;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pingpongx.flowmore.cloud.base.commom.exceptions.BaseErrorCode;
import com.pingpongx.flowmore.cloud.base.commom.exceptions.BaseRuntimeException;
import com.pingpongx.flowmore.cloud.base.commom.utils.PPConverter;
import com.pingpongx.smb.open.sdk.api.BaseResponse;
import com.pingpongx.smb.open.sdk.biz.data.v1.api.SmbDataQueryAPI;
import com.pingpongx.smb.open.sdk.biz.data.v1.request.SmbDataQueryByCodeRequest;
import com.pingpongx.smb.open.sdk.biz.data.v1.response.SmbDataQueryResponse;
import com.pingpongx.smb.open.sdk.core.client.SMBClient;
import com.pingpongx.smb.warning.web.module.CustomerInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class SMBDataClient {

    private final SMBClient smbClient;

    public List<CustomerInfo> queryCustomerInfo() {
        SmbDataQueryByCodeRequest request = new SmbDataQueryByCodeRequest();
        request.setCode("FM_CUSTOMER_LOST_V1");
        request.setCondition(new JSONObject());
        SmbDataQueryAPI dataApi = new SmbDataQueryAPI();
        dataApi.setAPIParams(request);
        BaseResponse<SmbDataQueryResponse> result;
        try {
            result = smbClient.invoke(dataApi);
            if (Objects.nonNull(result) && result.getSuccess()) {
                return PPConverter.toObject(result.getData().getResultList().toString(), new TypeReference<List<CustomerInfo>>() {});//.toJavaList(CustomerInfo.class);
            } else {
                log.error("smb-data信息查询失败, request:{}, result:{}", PPConverter.toJsonStringIgnoreException(request), PPConverter.toJsonStringIgnoreException(result));
                throw new BaseRuntimeException(result.getCode(), result.getMessage());
            }
        } catch (Exception e) {
            log.error("FM_CUSTOMER_LOST_V1, 查询smd data异常", e);
            throw new BaseRuntimeException(BaseErrorCode.BIZ_BREAK, "smb-data信息查询异常.", e);
        }
    }

}
