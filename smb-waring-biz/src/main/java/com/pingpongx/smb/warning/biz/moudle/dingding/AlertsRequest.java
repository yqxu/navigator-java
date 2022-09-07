package com.pingpongx.smb.warning.biz.moudle.dingding;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @Auther: jiangkun
 * @Date: 2022/06/22/10:32 上午
 * @Description:
 * @Version:
 */
@Data
public class AlertsRequest implements Serializable {


    private static final long serialVersionUID = -1670158377501813133L;
    private List<DingAlert> alerts;

}
