package com.pingpongx.smb.warning.biz.depends;

import com.pingpongx.smb.warning.biz.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:47 上午
 * @Description:
 * @Version:
 */
@Slf4j
@Component
public class B2BDingTalkClient extends AbstractPPDingTalkClient implements PPDingTalkClient {
    public String getNotifyUrl(){
        return Constant.B2B.url;
    }

    @Override
    public List<String> supportDepartNames() {
        return Stream.of(Constant.B2B.name,"Grafana-"+Constant.B2B.name).collect(Collectors.toList());
    }


    @Override
    public List<String> getAppNames() {
        return null;
    }
}
