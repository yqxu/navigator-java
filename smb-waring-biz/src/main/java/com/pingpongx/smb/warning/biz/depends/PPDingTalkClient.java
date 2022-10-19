package com.pingpongx.smb.warning.biz.depends;

import java.util.List;

public interface PPDingTalkClient {
    String getNotifyUrl();
    List<String> supportDepartNames();

    List<String> getAppNames();
    void sendMarkDown(String title,String content, List<String> notifyDingUserList);
    void sendDingTalkText(String content, List<String> notifyDingUserList);
}
