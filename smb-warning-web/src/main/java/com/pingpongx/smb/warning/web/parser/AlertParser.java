package com.pingpongx.smb.warning.web.parser;

import com.pingpongx.smb.warning.api.dto.JiraDTO;
import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;

import java.util.List;
import java.util.Set;

public interface AlertParser {
    ThirdPartAlert toAlert(String data);

    String toDingTalkMsg(ThirdPartAlert data);

    JiraGenerateRequest generateJiraRequest(ThirdPartAlert data);
    Set<String> getSupportDepart();
}
