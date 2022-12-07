package com.pingpongx.smb.warning.web.client;

import com.pingpongx.smb.warning.web.module.CustomerInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class SMBDataClient {

    public List<CustomerInfo> queryCustomerInfo(List<String> saleEmail) {
        return Lists.newArrayList();
    }


}
