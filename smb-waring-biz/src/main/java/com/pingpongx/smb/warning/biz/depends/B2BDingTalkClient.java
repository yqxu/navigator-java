package com.pingpongx.smb.warning.biz.depends;

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
public class B2BDingTalkClient extends AbstractPPDingTalkClient implements PPDingTalkClient {
    public String getNotifyUrl(){
        return Constant.B2B.name;
    }

    @Override
    public String getDepartName() {
        return Constant.B2B.name;
    }
}
