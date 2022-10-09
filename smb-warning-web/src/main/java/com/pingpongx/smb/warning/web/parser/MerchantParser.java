package com.pingpongx.smb.warning.web.parser;

import com.alibaba.fastjson.JSON;
import com.pingpongx.smb.warning.biz.alert.model.MerchantAlert;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.constant.Constant;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MerchantParser implements AlertParser {
    @Override
    public ThirdPartAlert toAlert(String data) {
        if (data == null){
            return null;
        }
        MerchantAlert ret = JSON.parseObject(data, MerchantAlert.class);
        return ret;
    }

    @Override
    public Set<String> getSupportDepart() {
        return Stream.of(Constant.Merchant.name).collect(Collectors.toSet());
    }
}
