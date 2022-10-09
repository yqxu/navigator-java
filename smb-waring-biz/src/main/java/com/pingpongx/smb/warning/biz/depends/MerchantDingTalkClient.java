package com.pingpongx.smb.warning.biz.depends;

import com.pingpongx.smb.warning.biz.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:47 上午
 * @Description:
 * @Version:
 */
@Slf4j
@Component
public class MerchantDingTalkClient extends AbstractPPDingTalkClient implements PPDingTalkClient {
    public String getNotifyUrl(){
        return Constant.Merchant.url;
    }
    @Override
    public String getDepartName() {
        return Constant.Merchant.name;
    }

    @Override
    public List<String> getAppNames() {
        return null;
    }
}
