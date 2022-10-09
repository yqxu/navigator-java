package com.pingpongx.smb.warning.biz.service;

import com.pingpongx.smb.warning.biz.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:47 上午
 * @Description:
 * @Version:
 */
@Slf4j
@Component
public class FlowMoreDingTalkClient extends AbstractPPDingTalkClient implements PPDingTalkClient {
    public String getNotifyUrl(){
        return Constant.FlowMore.url;
    }
}
