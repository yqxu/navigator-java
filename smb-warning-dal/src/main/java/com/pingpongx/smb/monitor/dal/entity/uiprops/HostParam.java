package com.pingpongx.smb.monitor.dal.entity.uiprops;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class HostParam {

    @Value("${uimonitor.env:local}")
    private String monitorEnv;

    @Value("${uimonitor.enable:false}")
    private String enable;

}
