package com.pingpongx.smb.warning.web.parser;

import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;

import java.util.List;
import java.util.Set;

public interface AlertParser {
    ThirdPartAlert toAlert(String data);
    Set<String> getSupportDepart();
}
