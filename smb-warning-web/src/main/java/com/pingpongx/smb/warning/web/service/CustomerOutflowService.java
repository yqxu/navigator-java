package com.pingpongx.smb.warning.web.service;

import com.fasterxml.jackson.core.type.TypeReference;
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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
public class CustomerOutflowService {

    private final PPUserClient ppUserClient;
    private final SMBDataClient smbDataClient;

    private final DingtalkClient dingtalkClient;

    private final Jira2vClient jira2vClient;

    private final Jira2vClientConfig jira2vClientConfig;

    private final CustomerOutflowDao customerOutflowDao;

    private final JiraInfoDao jiraInfoDao;

    @Autowired
    public CustomerOutflowService(PPUserClient ppUserClient, SMBDataClient smbDataClient, DingtalkClient dingtalkClient, Jira2vClient jira2vClient, Jira2vClientConfig jira2vClientConfig, CustomerOutflowDao customerOutflowDao, JiraInfoDao jiraInfoDao) {
        this.ppUserClient = ppUserClient;
        this.smbDataClient = smbDataClient;
        this.dingtalkClient = dingtalkClient;
        this.jira2vClient = jira2vClient;
        this.jira2vClientConfig = jira2vClientConfig;
        this.customerOutflowDao = customerOutflowDao;
        this.jiraInfoDao = jiraInfoDao;
    }

    @Setter
    @Getter
    @Value("${warnContent: 您名下无中高优先级的重点客户。具体客户明细请查看：https://data.pingpongx.com/#/report/858}")
    private String noHighLevelContent;

    @Setter
    @Getter

    @Value("${noticeContent:您名下有%s个中高优先级的重点客户，客户当前状态为正常，请继续保持。具体客户明细请查看：https://data.pingpongx.com/#/report/858}")
    private String highLevelContent;
    @Setter
    @Getter
    @Value("${noticeContent:您名下有%s个中高优先级的重点客户，重点客户明细请查看：https://data.pingpongx.com/#/report/858 \n 其中有%s个高危客户，存在流失风险，已为您生成对应gr工单，请及时跟进；\n %s}")
    private String highRiskLevelContent;
    @Setter
    @Getter
    @Value("${regularWarnContent:您还存在待跟进的⼯单，请及时跟进。\n %s}")
    private String regularWarnContent;

    @Setter
    @Getter
    @Value("${warnUser:}")
    private String warnUser;


    public List<PPUser> getWarnUsers() {
        if (StringUtils.isBlank(warnUser)) {
            return Lists.newArrayList();
        } else {
            return PPConverter.toObject(warnUser, new TypeReference<List<PPUser>>() {
            });
        }
    }

    /**
     * 流失预警
     */
    public void preWarnOfOutflow() {
        List<PPUser> ppUsers = getWarnUsers();
        if (ppUsers.isEmpty()) {
            ppUsers = ppUserClient.queryUserInfo();
        }
        if (ppUsers.isEmpty()) {
            log.info("预警人为空，结束预警");
            return;
        }
        Map<String, PPUser> ppUserMap = ppUsers.stream().collect(Collectors.toMap(PPUser::getEmail, Function.identity(), (k1, k2) -> k1));
        List<CustomerInfo> customerInfos = smbDataClient.queryCustomerInfo();
        //获取P0 P1用户
        customerInfos = customerInfos.stream().filter(customerInfo -> "P0".equals(customerInfo.getClientPriorityLevel()) || "P1".equals(customerInfo.getClientPriorityLevel())).collect(Collectors.toList());
        if (customerInfos.isEmpty()) {
            log.info("预警数据为空，结束预警");
            return;
        }
        Map<String, List<CustomerInfo>> customerInfoGroup = customerInfos.stream().collect(Collectors.groupingBy(CustomerInfo::getSalesEmail));

        //获取P0 P1用户邮箱
        Set<String> P0P1Email = customerInfos.stream().map(CustomerInfo::getSalesEmail).collect(Collectors.toSet());
        //非P0 P1用户邮箱
        Set<String> notP0P1Email = ppUserMap.keySet().stream().filter(s -> !P0P1Email.contains(s)).collect(Collectors.toSet());
        for (String email : notP0P1Email) {
            PPUser ppUser = ppUserMap.get(email);
            if (ppUser != null) {
                log.info("{} 您名下无中高优先级的重点客户", email);
                dingtalkClient.sendContent(ppUser.getUserid(), noHighLevelContent);
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
                    dingtalkClient.sendContent(ppUser.getUserid(), String.format(highLevelContent, entry.getValue().size()));
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
                    String jira = Joiner.on(" \n").join(customerOrderInfos.stream().map(CustomerOrderInfo::getUrl).collect(Collectors.toList()));
                    dingtalkClient.sendContent(ppUser.getUserid(), String.format(highRiskLevelContent, entry.getValue().size(), highRisk.size(), jira));
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
            log.info("创建jira工单, clientId = {}, email = {}", customerInfo.getClientId(), ppUser.getEmail());
            MeterRegistryProvider.provider().counter(outflow, Tags.of(MeterTag.status, MeterTagValue.Status.start)).increment();
            CustomerWarnJira field = new CustomerWarnJira();
            String summary = String.format(jira2vClientConfig.getSummary(), customerInfo.getClientPriorityLevel(), customerInfo.getClientId());
            field.setSummary(summary);
            field.setDescription(jira2vClientConfig.getDesc());
            field.setIssuetype(new Field.Name(jira2vClientConfig.getIssuetype()));
            field.setProject(new Field.Key(jira2vClientConfig.getProject()));
            field.setAssignee(new Field.Name(ppUser.getDomainAccount()));

            long orderId = UUIDUtils.nextId();
            field.setOrderId("" + orderId);
            field.setClientId(customerInfo.getClientId());

            field.setLevel(new Field.Value(StringUtils.isBlank(customerInfo.getClientPriorityLevel()) ? "-1" : customerInfo.getClientPriorityLevel()));
            field.setKaType(new Field.Value(StringUtils.isBlank(customerInfo.getKaType()) ? "-1" : customerInfo.getKaType()));
            field.setIndustryLevel(StringUtils.isBlank(customerInfo.getCategory()) ? "-1" : customerInfo.getCategory());
            field.setOutflowStatus(new Field.Value(StringUtils.isBlank(customerInfo.getClientLostStatus()) ? "-1" : customerInfo.getClientLostStatus()));
            field.setOutflowTag(new Field.Value(StringUtils.isBlank(customerInfo.getClientLostWorthStatus()) ? "-1" : customerInfo.getClientLostWorthStatus()));
            field.setActiveStatus(new Field.Value(StringUtils.isBlank(customerInfo.getClientActiveStatus()) ? "-1" : customerInfo.getClientActiveStatus()));
            field.setActiveTag(new Field.Value(StringUtils.isBlank(customerInfo.getClientActiveWorthStatus()) ? "-1" : customerInfo.getClientActiveWorthStatus()));
            field.setFollowUp(new Field.Name(ppUser.getDomainAccount()));
            field.setAvgTradeNum(customerInfo.getAvgInboundCnt3sm() == null ? new BigDecimal("-1") : customerInfo.getAvgInboundCnt3sm());

            IssueReq issueReq = new IssueReq();
            issueReq.setFields(field);
            IssueResp issueResp = jira2vClient.createIssue(issueReq);
            String url = jira2vClientConfig.getBaseUrl() + "/browse/" + issueResp.getKey();
            log.info("issueResp = {}, url = {}", PPConverter.toJsonStringIgnoreException(issueResp), url);
            CustomerOrderInfo customerOrderInfo = TransferUtils.newInstance(CustomerOrderInfo::new, d -> {
                d.setClientId(customerInfo.getClientId());
                d.setIssueId(issueResp.getId());
                d.setSalesEmail(customerInfo.getSalesEmail());
                d.setProjectKey(jira2vClientConfig.getProject());
                d.setAssignee(ppUser.getDomainAccount());
                d.setPriorityLevel(customerInfo.getClientPriorityLevel());
                d.setSummary(summary);
                d.setIssueType(jira2vClientConfig.getIssuetype());
                d.setUrl(url);
                d.setStatus("待解决");
                d.setOrderId(orderId);
            });
            customerOutflowDao.save(customerOrderInfo);
            MeterRegistryProvider.provider().counter(outflow, Tags.of(MeterTag.status, MeterTagValue.Status.success)).increment();
            log.info("创建jira工单完成, clientId = {}, email = {}", customerInfo.getClientId(), ppUser.getEmail());
            return customerOrderInfo;
        } catch (Exception e) {
            log.error("创建jira工单异常, clientId = {}, email = {}", customerInfo.getClientId(), ppUser.getEmail(), e);
            MeterRegistryProvider.provider().counter(outflow, Tags.of(MeterTag.status, MeterTagValue.Status.fail)).increment();
            return null;
        }
    }


    /**
     * 定期预警
     */
    public void regularWarn() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.ofHours(8));
        zonedDateTime = zonedDateTime.minusDays(7);
        List<JiraInfo> P0jiraInfos = jiraInfoDao.query(jira2vClientConfig.getProject(), "已完成", JiraInfo.PriorityLevel.P0.name(), LocalDateUtils.time(zonedDateTime));
        zonedDateTime = zonedDateTime.minusDays(7);
        List<JiraInfo> P1jiraInfos = jiraInfoDao.query(jira2vClientConfig.getProject(), "已完成", JiraInfo.PriorityLevel.P1.name(), LocalDateUtils.time(zonedDateTime));
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
                    String jira = Joiner.on(" \n").join(entry.getValue().stream().map(JiraInfo::getUrl).collect(Collectors.toList()));
                    dingtalkClient.sendContent(ppUser.getUserid(), String.format(regularWarnContent, jira));
                } else {
                    log.info("未找到相关运营人员，不通知, {}", entry.getKey());
                }
            }
        } else {
            log.info("数据为空，没有巡检");
        }
    }
}
