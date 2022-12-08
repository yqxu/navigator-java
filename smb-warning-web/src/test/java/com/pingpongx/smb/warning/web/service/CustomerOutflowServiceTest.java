package com.pingpongx.smb.warning.web.service;

import com.pingpongx.flowmore.cloud.base.commom.utils.PPConverter;
import com.pingpongx.flowmore.cloud.base.server.provider.MeterRegistryProvider;
import com.pingpongx.smb.cloud.client.Jira2vClient;
import com.pingpongx.smb.cloud.dingtalk.client.DingtalkClient;
import com.pingpongx.smb.cloud.model.IssueReq;
import com.pingpongx.smb.cloud.model.IssueResp;
import com.pingpongx.smb.organization.common.resp.PPUser;
import com.pingpongx.smb.warning.web.client.PPUserClient;
import com.pingpongx.smb.warning.web.client.SMBDataClient;
import com.pingpongx.smb.warning.web.config.Jira2vClientConfig;
import com.pingpongx.smb.warning.web.dao.CustomerOutflowDao;
import com.pingpongx.smb.warning.web.dao.JiraInfoDao;
import com.pingpongx.smb.warning.web.module.CustomerInfo;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@Slf4j
public class CustomerOutflowServiceTest {

    @InjectMocks
    private CustomerOutflowService customerOutflowService;

    @Mock
    private PPUserClient ppUserClient;

    @Mock
    private SMBDataClient smbDataClient;

    @Spy
    private DingtalkClient dingtalkClient = new DingtalkClient("https://api.dingtalk.com", "https://oapi.dingtalk.com", "dingnxspdqi3yf4cwzqp", "2QVTrXwizakEKliyvqr-k3Q8Ji5oWUKxFLRCB3zWDIqi3jrHzFh8JGXr83ZoqTOG");

    @Spy
    private Jira2vClient jira2vClient = new Jira2vClient("http://jira.pingpongx.com", "MTAwODgyMDk4MTY3OuZqSyEfQLeo/IFKtYhwSudgjme0");


    @Mock
    private Jira2vClientConfig jira2vClientConfig;

    @Mock
    private CustomerOutflowDao customerOutflowDao;

    @Mock
    private JiraInfoDao jiraInfoDao;

    @BeforeClass
    public static void beforeClass() {
        MeterRegistryProvider.put(new SimpleMeterRegistry());
    }

    @Before
    public void before() {
        customerOutflowService.setHighLevelContent("您名下有%s个中高优先级的重点客户，客户当前状态为正常，请继续保持。具体客户明细请查看：https://data.pingpongx.com/#/report/858");
        customerOutflowService.setNoHighLevelContent("您名下无中高优先级的重点客户。具体客户明细请查看：https://data.pingpongx.com/#/report/858");
        customerOutflowService.setHighRiskLevelContent("您名下有%s个中高优先级的重点客户，重点客户明细请查看：https://data.pingpongx.com/#/report/858 \n 其中有%s个高危客户，存在流失风险，已为您生成对应gr工单，请及时跟进；\n %s");
        customerOutflowService.setRegularWarnContent("您还存在待跟进的⼯单，请及时跟进。\n %s");

        when(jira2vClientConfig.getSummary()).thenReturn("%s优先级客户%s，存在流失风险，请及时跟进");
        when(jira2vClientConfig.getDesc()).thenReturn("{color:#FF0000}请及时跟进存在流失风险的重点客户，并认真填写工单信息；P0级别客户需要任务生成1周后完成；P1级别客户需要任务生成2周后完成！！！{color}");
        when(jira2vClientConfig.getProject()).thenReturn("RECALL");
        when(jira2vClientConfig.getIssuetype()).thenReturn("线上工单");
        when(jira2vClientConfig.getBaseUrl()).thenReturn("http://jira.pingpongx.com");

    }

    @Test
    public void test_preWarnOfOutflow() {
        PPUser ppUser = new PPUser();
        ppUser.setDomainAccount("jianggm");
        ppUser.setEmail("jianggm@pingpongx.com");
        ppUser.setUserid("01193837751079");


        when(ppUserClient.queryUserInfo()).thenReturn(Lists.newArrayList(ppUser));

        List<CustomerInfo> list = Lists.newArrayList();
        CustomerInfo customerInfo = new CustomerInfo();
        customerInfo.setClientId("862203040408311628");
        customerInfo.setClientLostStatus("活跃");
        customerInfo.setClientLostWorthStatus("中价值");
        customerInfo.setClientActiveStatus("低活");
        customerInfo.setClientActiveWorthStatus("低价值");
        customerInfo.setClientPriorityLevel("P1");
        customerInfo.setIfHighRisk(1);
        customerInfo.setSalesEmail("jianggm1@pingpongx.com");
        customerInfo.setKaType("ka");
        customerInfo.setCategory("玩具、运动和爱好物品:乐器");
        customerInfo.setAvgInboundCnt3sm(10);
        list.add(customerInfo);

        customerInfo = new CustomerInfo();
        customerInfo.setClientId("8622030404083116287");
        customerInfo.setClientLostStatus("活跃");
        customerInfo.setClientLostWorthStatus("中价值");
        customerInfo.setClientActiveStatus("低活");
        customerInfo.setClientActiveWorthStatus("低价值");
        customerInfo.setClientPriorityLevel("P1");
        customerInfo.setIfHighRisk(0);
        customerInfo.setSalesEmail("jianggm1@pingpongx.com");
        customerInfo.setKaType("ka");
        customerInfo.setCategory("玩具、运动和爱好物品:乐器");
        customerInfo.setAvgInboundCnt3sm(10);
        list.add(customerInfo);

        customerInfo = new CustomerInfo();
        customerInfo.setClientId("862203040408311629");
        customerInfo.setClientLostStatus("活跃");
        customerInfo.setClientLostWorthStatus("中价值");
        customerInfo.setClientActiveStatus("低活");
        customerInfo.setClientActiveWorthStatus("低价值");
        customerInfo.setClientPriorityLevel("P2");
        customerInfo.setIfHighRisk(0);
        customerInfo.setSalesEmail("jianggm1@pingpongx.com");
        customerInfo.setKaType("ka");
        customerInfo.setCategory("玩具、运动和爱好物品:乐器");
        customerInfo.setAvgInboundCnt3sm(10);
        list.add(customerInfo);

        customerInfo = new CustomerInfo();
        customerInfo.setClientId("862203040408311630");
        customerInfo.setClientLostStatus("活跃");
        customerInfo.setClientLostWorthStatus("中价值");
        customerInfo.setClientActiveStatus("低活");
        customerInfo.setClientActiveWorthStatus("低价值");//中价值
        customerInfo.setClientPriorityLevel("P0");
        customerInfo.setIfHighRisk(0);
        customerInfo.setSalesEmail("jianggm1@pingpongx.com");
        customerInfo.setKaType("ka");
        customerInfo.setCategory("玩具、运动和爱好物品:乐器");
        customerInfo.setAvgInboundCnt3sm(10);
        list.add(customerInfo);
        when(smbDataClient.queryCustomerInfo()).thenReturn(list);

/*        when(dingtalkClient.sendContent(anyString(), anyString())).thenAnswer(invocationOnMock -> {
            Object[] objects = invocationOnMock.getArguments();
            String userid = (String) objects[0];
            String content = (String) objects[1];
            Assert.assertTrue("发送钉钉消息，userid异常:" + userid, "122912174322778022".equals(userid) || "122912174322778041".equals(userid) || "122912174322778042".equals(userid));
            log.info("发送钉钉消息成功:{},{}", userid, content);
            return null;
        });*/

        /*when(jira2vClient.createIssue(any())).thenAnswer(invocationOnMock -> {
            Object[] objects = invocationOnMock.getArguments();
            IssueReq issueReq = (IssueReq) objects[0];
            Assert.assertEquals("assignee", "jianggm", issueReq.getFields().getAssignee().getName());
            log.info("发送jira消息成功:{}", PPConverter.toJsonStringIgnoreException(issueReq));
            IssueResp issueResp = new IssueResp();
            issueResp.setId("123");
            issueResp.setKey("RECALL-1111");
            issueResp.setSelf("http://RECALL");
            return new IssueResp();
        });*/
        doNothing().when(customerOutflowDao).save(any());
        customerOutflowService.preWarnOfOutflow();
/*        verify(dingtalkClient, times(3)).sendContent(any(), anyString());
        verify(jira2vClient, times(2)).createIssue(any());
        verify(customerOutflowDao, times(2)).save(any());
        verify(smbDataClient, times(1)).queryCustomerInfo();
        verify(ppUserClient, times(1)).queryUserInfo();*/
    }


}
