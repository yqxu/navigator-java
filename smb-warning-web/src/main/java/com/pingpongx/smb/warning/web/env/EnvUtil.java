package com.pingpongx.smb.warning.web.env;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvUtil {
    @Value("${ppx.environment}")
    private String envStr;
    public boolean isDev(){
        return envStr.startsWith("dev");
    }
}
