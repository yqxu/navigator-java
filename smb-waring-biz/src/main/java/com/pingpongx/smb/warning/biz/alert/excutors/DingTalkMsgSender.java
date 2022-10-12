package com.pingpongx.smb.warning.biz.alert.excutors;

import com.pingpongx.smb.warning.biz.depends.DingTalkClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DingTalkMsgSender {
    @Autowired
    DingTalkClientFactory clientFactory;

}
