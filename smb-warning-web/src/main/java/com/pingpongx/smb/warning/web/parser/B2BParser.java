package com.pingpongx.smb.warning.web.parser;

import com.alibaba.fastjson.JSON;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.biz.constant.Constant;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class B2BParser implements AlertParser {
    @Override
    public ThirdPartAlert toAlert(String data) {
        if (data == null){
            return null;
        }
        SlsAlert ret = JSON.parseObject(data, SlsAlert.class);
        return ret;
    }

    @Override
    public Set<String> getSupportDepart() {
        return Stream.of(Constant.Mid.name,Constant.B2B.name,Constant.FlowMore.name).collect(Collectors.toSet());
    }
}
