package com.pingpongx.smb.warning.biz.alert.threshold;

import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DubboTimeOutInhibition<T extends ThirdPartAlert> extends SimpleCounterThresholdInhibition<T>{

}
