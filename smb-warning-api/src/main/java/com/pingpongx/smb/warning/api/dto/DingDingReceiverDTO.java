package com.pingpongx.smb.warning.api.dto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/06/7:52 下午
 * @Description:
 * @Version:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DingDingReceiverDTO implements Serializable {

    private static final long serialVersionUID = -1659861398946615527L;

    private List<DingDReceiverDTO> receivers;

    public static DingDingReceiverDTO defaultReceiver(){
        return DingDingReceiverDTO.builder().receivers(Collections.singletonList(DingDReceiverDTO.getDefaultUser())).build();
    }
}
