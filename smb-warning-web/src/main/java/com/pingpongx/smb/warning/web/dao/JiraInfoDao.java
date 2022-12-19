package com.pingpongx.smb.warning.web.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.pingpongx.smb.warning.dal.dataobject.JiraInfo;
import com.pingpongx.smb.warning.dal.repository.JiraInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JiraInfoDao {

    private final JiraInfoRepository jiraInfoRepository;

    //更新jira工单状态
    public void updateStatus(String status, String issueId, String projectKey) {
        UpdateWrapper<JiraInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("issueId", issueId);
        updateWrapper.eq("projectKey", projectKey);
        JiraInfo jiraInfo = new JiraInfo();
        jiraInfo.setStatus(status);
        jiraInfoRepository.update(jiraInfo, updateWrapper);
    }

    public List<JiraInfo> query(String projectKey, String status, String priorityLevel, Date created) {
        QueryWrapper<JiraInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("projectKey", projectKey);
        queryWrapper.ne("status", status);
        queryWrapper.eq("priorityLevel", priorityLevel);
        queryWrapper.le("created", created);
        return jiraInfoRepository.list(queryWrapper);
    }
}
