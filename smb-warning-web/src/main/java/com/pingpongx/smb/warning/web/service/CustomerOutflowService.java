package com.pingpongx.smb.warning.web.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.pingpongx.flowmore.cloud.base.commom.constants.MeterTag;
import com.pingpongx.flowmore.cloud.base.commom.constants.MeterTagValue;
import com.pingpongx.flowmore.cloud.base.commom.utils.LocalDateUtils;
import com.pingpongx.flowmore.cloud.base.commom.utils.PPConverter;
import com.pingpongx.flowmore.cloud.base.commom.utils.UUIDUtils;
import com.pingpongx.flowmore.cloud.base.server.provider.MeterRegistryProvider;
import com.pingpongx.flowmore.cloud.base.server.utils.TransferUtils;
import com.pingpongx.smb.cloud.client.Jira2vClient;
import com.pingpongx.smb.cloud.dingtalk.client.DingtalkClient;
import com.pingpongx.smb.cloud.model.Field;
import com.pingpongx.smb.cloud.model.IssueReq;
import com.pingpongx.smb.cloud.model.IssueResp;
import com.pingpongx.smb.organization.common.resp.PPUser;
import com.pingpongx.smb.warning.dal.dataobject.CustomerOrderInfo;
import com.pingpongx.smb.warning.dal.dataobject.JiraInfo;
import com.pingpongx.smb.warning.web.client.PPUserClient;
import com.pingpongx.smb.warning.web.client.SMBDataClient;
import com.pingpongx.smb.warning.web.config.Jira2vClientConfig;
import com.pingpongx.smb.warning.web.dao.CustomerOutflowDao;
import com.pingpongx.smb.warning.web.dao.JiraInfoDao;
import com.pingpongx.smb.warning.web.module.CustomerInfo;
import com.pingpongx.smb.warning.web.module.CustomerWarnJira;
import io.micrometer.core.instrument.Tags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 客户流失操作
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerOutflowService {

    private final PPUserClient ppUserClient;
    private final SMBDataClient smbDataClient;

    private final DingtalkClient dingtalkClient;

    private final Jira2vClient jira2vClient;

    private final Jira2vClientConfig jira2vClientConfig;

    private final CustomerOutflowDao customerOutflowDao;

    private final JiraInfoDao jiraInfoDao;

    @Value("${warnContent: 您名下无中高优先级的重点客户。}")
    private String noHighLevelContent;

    @Value("${noticeContent:您名下有%s个中高优先级的重点客户，客户当前状态为正常，请继续保持。}")
    private String highLevelContent;

    @Value("${noticeContent:您名下有%s个中高优先级的重点客户，其中有%s个高危客户，存在流失风险，已为您生成对应gr工单，请及时跟进。\n %s}")
    private String highRiskLevelContent;

    @Value("${regularWarnContent:您还存在待跟进的⼯单，请及时跟进。\n %s}")
    private String regularWarnContent;

    /**
     * 流失预警
     */
    public void preWarnOfOutflow() {
        List<PPUser> ppUsers = ppUserClient.queryUserInfo();
        Map<String, PPUser> ppUserMap = ppUsers.stream().collect(Collectors.toMap(PPUser::getEmail, Function.identity(), (k1, k2) -> k1));
        List<CustomerInfo> customerInfos = smbDataClient.queryCustomerInfo(Lists.newArrayList(ppUserMap.keySet()));
        //获取P0 P1用户
        customerInfos = customerInfos.stream().filter(customerInfo -> "P0".equals(customerInfo.getClientPriorityLevel()) || "P1".equals(customerInfo.getClientPriorityLevel())).collect(Collectors.toList());
        Map<String, List<CustomerInfo>> customerInfoGroup = customerInfos.stream().collect(Collectors.groupingBy(CustomerInfo::getSalesEmail));

        //获取P0 P1用户邮箱
        Set<String> P0P1Email = customerInfos.stream().map(CustomerInfo::getSalesEmail).collect(Collectors.toSet());
        //非P0 P1用户邮箱
        Set<String> notP0P1Email = ppUserMap.keySet().stream().filter(s -> !P0P1Email.contains(s)).collect(Collectors.toSet());
        for (String email : notP0P1Email) {
            PPUser ppUser = ppUserMap.get(email);
            if (ppUser != null) {
                log.info("{} 您名下无中高优先级的重点客户", email);
                dingtalkClient.batchSend(ppUser.getUserid(), noHighLevelContent);
            } else {
                log.info("{} 未找到此人..0", email);
            }
        }
        for (Map.Entry<String, List<CustomerInfo>> entry : customerInfoGroup.entrySet()) {
            //高危客户
            List<CustomerInfo> highRisk = entry.getValue().stream().filter(customerInfo -> 1 == customerInfo.getIfHighRisk()).collect(Collectors.toList());
            //低危客户
            List<CustomerInfo> lowRisk = entry.getValue().stream().filter(customerInfo -> 0 == customerInfo.getIfHighRisk()).collect(Collectors.toList());
            if (highRisk.isEmpty()) {
                PPUser ppUser = ppUserMap.get(entry.getKey());
                if (ppUser != null) {
                    log.info("{}, 您名下有{}个中高优先级的重点客户，客户当前状态为正常，请继续保持。", entry.getKey(), lowRisk.size());
                    dingtalkClient.batchSend(ppUser.getUserid(), String.format(highLevelContent, entry.getValue().size()));
                } else {
                    log.info("{} 未找到此人..1", entry.getKey());
                }
            } else {
                //高危客户处理
                PPUser ppUser = ppUserMap.get(entry.getKey());
                if (ppUser != null) {
                    log.info("{}, 您名下有{}个中高优先级的重点客户，其中有{}个高危客户，存在流失风险，已为您生成对应gr工单，请及时跟进。", entry.getKey(), entry.getValue().size(), highRisk.size());
                    List<CustomerOrderInfo> customerOrderInfos = Lists.newArrayList();
                    for (CustomerInfo customerInfo : highRisk) {
                        CustomerOrderInfo customerOrderInfo = createOrder(customerInfo, ppUser);
                        if (customerOrderInfo != null) {
                            customerOrderInfos.add(customerOrderInfo);
                        }
                    }
                    String jira = Joiner.on(",").join(customerOrderInfos.stream().map(CustomerOrderInfo::getUrl).collect(Collectors.toList()));
                    dingtalkClient.batchSend(ppUser.getUserid(), String.format(highRiskLevelContent, entry.getValue().size(), highRisk.size(), jira));
                } else {
                    log.info("{} 未找到此人..2", entry.getKey());
                }

            }
        }
    }

    private final static String outflow = "customer_outflow_jira2v";

    //创建jira工单
    private CustomerOrderInfo createOrder(CustomerInfo customerInfo, PPUser ppUser) {
        try {
            log.info("创建jira工单, clientId = {}, email = {}", customerInfo.getClientid(), ppUser.getEmail());
            MeterRegistryProvider.provider().counter(outflow, Tags.of(MeterTag.status, MeterTagValue.Status.start)).increment();
            CustomerWarnJira field = new CustomerWarnJira();
            String summary = String.format(jira2vClientConfig.getSummary(), customerInfo.getClientPriorityLevel(), customerInfo.getClientid());
            field.setSummary(summary);
            field.setDescription(jira2vClientConfig.getDesc());
            field.setIssuetype(new Field.Name(jira2vClientConfig.getIssuetype()));
            field.setProject(new Field.Key(jira2vClientConfig.getProject()));
            field.setAssignee(new Field.Name(ppUser.getDomainAccount()));

            long orderId = UUIDUtils.nextId();
            field.setOrderId("" + orderId);
            field.setClientId(customerInfo.getClientid());
            field.setLevel(new Field.Value(customerInfo.getClientPriorityLevel()));
            field.setKaType(new Field.Value(customerInfo.getKaType()));
            field.setIndustryLevel(customerInfo.getCategory());
            field.setOutflowStatus(new Field.Value(customerInfo.getClientLostStatus()));
            field.setOutflowTag(new Field.Value(customerInfo.getClientLostWorthStatus()));
            field.setActiveStatus(new Field.Value(customerInfo.getClientActiveStatus()));
            field.setActiveTag(new Field.Value(customerInfo.getClientActiveWorthStatus()));
            field.setAvgTradeNum(customerInfo.getAvgInboundCnt3sm());
            field.setFollowUp(new Field.Name(ppUser.getDomainAccount()));

            IssueReq issueReq = new IssueReq();
            issueReq.setFields(field);
            IssueResp issueResp = jira2vClient.createIssue(issueReq);
            log.info("issueResp = {}", PPConverter.toJsonStringIgnoreException(issueResp));
            CustomerOrderInfo customerOrderInfo = TransferUtils.newInstance(CustomerOrderInfo::new, d -> {
                d.setClientId(customerInfo.getClientid());
                d.setIssueId(issueResp.getId());
                d.setSalesEmail(customerInfo.getSalesEmail());
                d.setProjectKey(jira2vClientConfig.getProject());
                d.setAssignee(ppUser.getDomainAccount());
                d.setPriorityLevel(customerInfo.getClientPriorityLevel());
                d.setSummary(summary);
                d.setIssueType(jira2vClientConfig.getIssuetype());
                d.setUrl(issueResp.getSelf());
                d.setStatus("待解决");
                d.setOrderId(orderId);
            });
            customerOutflowDao.save(customerOrderInfo);
            MeterRegistryProvider.provider().counter(outflow, Tags.of(MeterTag.status, MeterTagValue.Status.success)).increment();
            log.info("创建jira工单完成, clientId = {}, email = {}", customerInfo.getClientid(), ppUser.getEmail());
            return customerOrderInfo;
        } catch (Exception e) {
            MeterRegistryProvider.provider().counter(outflow, Tags.of(MeterTag.status, MeterTagValue.Status.fail)).increment();
            log.error("创建jira工单异常, clientId = {}, email = {}", customerInfo.getClientid(), ppUser.getEmail());
            return null;
        }
    }


    /**
     * 定期预警
     */
    public void regularWarn() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.ofHours(8));
        zonedDateTime = zonedDateTime.minusDays(7);
        List<JiraInfo> P0jiraInfos = jiraInfoDao.query(jira2vClientConfig.getProject(), "待解决", JiraInfo.PriorityLevel.P0.name(), LocalDateUtils.time(zonedDateTime));
        zonedDateTime = zonedDateTime.minusDays(7);
        List<JiraInfo> P1jiraInfos = jiraInfoDao.query(jira2vClientConfig.getProject(), "待解决", JiraInfo.PriorityLevel.P0.name(), LocalDateUtils.time(zonedDateTime));
        List<JiraInfo> jiraInfos = Lists.newArrayList();
        jiraInfos.addAll(P0jiraInfos);
        jiraInfos.addAll(P1jiraInfos);
        if (!jiraInfos.isEmpty()) {
            List<PPUser> ppUsers = ppUserClient.queryUserInfo();
            Map<String, PPUser> ppUserMap = ppUsers.stream().collect(Collectors.toMap(PPUser::getDomainAccount, Function.identity(), (k1, k2) -> k1));
            Map<String, List<JiraInfo>> jiraInfoMap = jiraInfos.stream().collect(Collectors.groupingBy(JiraInfo::getAssignee));
            for (Map.Entry<String, List<JiraInfo>> entry : jiraInfoMap.entrySet()) {
                PPUser ppUser = ppUserMap.get(entry.getKey());
                if (ppUser != null) {
                    String jira = Joiner.on(",").join(entry.getValue().stream().map(JiraInfo::getUrl).collect(Collectors.toList()));
                    dingtalkClient.batchSend(ppUser.getUserid(), String.format(regularWarnContent, jira));
                } else {
                    log.info("未找到相关运营人员，不通知, {}", entry.getKey());
                }
            }
        }
    }
}
