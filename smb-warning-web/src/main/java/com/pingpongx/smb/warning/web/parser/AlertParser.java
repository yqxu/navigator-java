package com.pingpongx.smb.warning.web.parser;

import com.pingpongx.smb.warning.api.request.JiraGenerateRequest;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface AlertParser {
    List<ThirdPartAlert> toAlerts(String data);

    String toDingTalkMsg(ThirdPartAlert data);

    JiraGenerateRequest generateJiraRequest(ThirdPartAlert data);
    Set<String> getSupportDepart();

    default List<ThirdPartAlert> parse(String data){
        return toAlerts(data);
    }
}
