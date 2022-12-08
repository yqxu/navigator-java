package com.pingpongx.com.smb.warning.web;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pingpongx.flowmore.cloud.base.commom.utils.PPConverter;
import com.pingpongx.smb.open.sdk.core.client.DefaultSMBClient;
import com.pingpongx.smb.warning.web.client.SMBDataClient;
import com.pingpongx.smb.warning.web.module.CustomerInfo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class SMBDataClientTest {

    SMBDataClient smbDataClient = new SMBDataClient(new DefaultSMBClient("https://test2-smb-inner-gateway.pingpongx.com", "f32793b52cc0263ca0e4ff7ad71ab005", "4e7f5f9f0e3ec18daa4d0ce4de944320"));

    @Test
    public void test() {
        List<CustomerInfo> customerInfos = smbDataClient.queryCustomerInfo();
        System.out.println(PPConverter.toJsonStringIgnoreException(customerInfos));
    }

    @Test
    public void test01() {
        String message = "{\n" +
                "    \"timestamp\": 1670404909530,\n" +
                "    \"webhookEvent\": \"jira:issue_updated\",\n" +
                "    \"issue_event_type_name\": \"issue_generic\",\n" +
                "    \"user\": {\n" +
                "        \"self\": \"http://jira.pingpongx.com/rest/api/2/user?username=jianggm\",\n" +
                "        \"name\": \"jianggm\",\n" +
                "        \"key\": \"jianggm\",\n" +
                "        \"emailAddress\": \"jianggm@pingpongx.com\",\n" +
                "        \"avatarUrls\": {\n" +
                "            \"48x48\": \"http://jira.pingpongx.com/secure/useravatar?avatarId=10122\",\n" +
                "            \"24x24\": \"http://jira.pingpongx.com/secure/useravatar?size=small&avatarId=10122\",\n" +
                "            \"16x16\": \"http://jira.pingpongx.com/secure/useravatar?size=xsmall&avatarId=10122\",\n" +
                "            \"32x32\": \"http://jira.pingpongx.com/secure/useravatar?size=medium&avatarId=10122\"\n" +
                "        },\n" +
                "        \"displayName\": \"姜光明\",\n" +
                "        \"active\": true,\n" +
                "        \"timeZone\": \"Asia/Shanghai\"\n" +
                "    },\n" +
                "    \"issue\": {\n" +
                "        \"id\": \"96122\",\n" +
                "        \"self\": \"http://jira.pingpongx.com/rest/api/2/issue/96122\",\n" +
                "        \"key\": \"RECALL-4\",\n" +
                "        \"fields\": {\n" +
                "            \"fixVersions\": [],\n" +
                "            \"resolution\": null,\n" +
                "            \"customfield_10500\": null,\n" +
                "            \"customfield_10503\": null,\n" +
                "            \"customfield_10504\": null,\n" +
                "            \"customfield_10900\": null,\n" +
                "            \"customfield_10901\": null,\n" +
                "            \"customfield_10903\": null,\n" +
                "            \"customfield_10904\": null,\n" +
                "            \"customfield_10905\": null,\n" +
                "            \"customfield_10906\": null,\n" +
                "            \"customfield_10907\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/customFieldOption/10969\",\n" +
                "                \"value\": \"否\",\n" +
                "                \"id\": \"10969\",\n" +
                "                \"disabled\": false\n" +
                "            },\n" +
                "            \"lastViewed\": \"2022-12-07T17:21:49.506+0800\",\n" +
                "            \"customfield_10220\": null,\n" +
                "            \"priority\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/priority/3\",\n" +
                "                \"iconUrl\": \"http://jira.pingpongx.com/images/icons/priorities/medium.svg\",\n" +
                "                \"name\": \"P1\",\n" +
                "                \"id\": \"3\"\n" +
                "            },\n" +
                "            \"labels\": [],\n" +
                "            \"customfield_10610\": null,\n" +
                "            \"customfield_10216\": null,\n" +
                "            \"customfield_10612\": null,\n" +
                "            \"customfield_10614\": null,\n" +
                "            \"timeestimate\": null,\n" +
                "            \"aggregatetimeoriginalestimate\": null,\n" +
                "            \"versions\": [],\n" +
                "            \"customfield_10219\": null,\n" +
                "            \"customfield_10615\": null,\n" +
                "            \"issuelinks\": [],\n" +
                "            \"assignee\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/user?username=wangjl\",\n" +
                "                \"name\": \"wangjl\",\n" +
                "                \"key\": \"wangjl\",\n" +
                "                \"emailAddress\": \"wangjl@pingpongx.com\",\n" +
                "                \"avatarUrls\": {\n" +
                "                    \"48x48\": \"http://jira.pingpongx.com/secure/useravatar?ownerId=wangjl&avatarId=11525\",\n" +
                "                    \"24x24\": \"http://jira.pingpongx.com/secure/useravatar?size=small&ownerId=wangjl&avatarId=11525\",\n" +
                "                    \"16x16\": \"http://jira.pingpongx.com/secure/useravatar?size=xsmall&ownerId=wangjl&avatarId=11525\",\n" +
                "                    \"32x32\": \"http://jira.pingpongx.com/secure/useravatar?size=medium&ownerId=wangjl&avatarId=11525\"\n" +
                "                },\n" +
                "                \"displayName\": \"王军龙\",\n" +
                "                \"active\": true,\n" +
                "                \"timeZone\": \"Asia/Shanghai\"\n" +
                "            },\n" +
                "            \"status\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/status/10604\",\n" +
                "                \"description\": \"\",\n" +
                "                \"iconUrl\": \"http://jira.pingpongx.com/images/icons/status_generic.gif\",\n" +
                "                \"name\": \"解决中\",\n" +
                "                \"id\": \"10604\",\n" +
                "                \"statusCategory\": {\n" +
                "                    \"self\": \"http://jira.pingpongx.com/rest/api/2/statuscategory/4\",\n" +
                "                    \"id\": 4,\n" +
                "                    \"key\": \"indeterminate\",\n" +
                "                    \"colorName\": \"yellow\",\n" +
                "                    \"name\": \"处理中\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"components\": [],\n" +
                "            \"customfield_11301\": null,\n" +
                "            \"archiveddate\": null,\n" +
                "            \"customfield_10600\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/customFieldOption/10601\",\n" +
                "                \"value\": \"否\",\n" +
                "                \"id\": \"10601\",\n" +
                "                \"disabled\": false\n" +
                "            },\n" +
                "            \"customfield_10206\": null,\n" +
                "            \"customfield_10602\": null,\n" +
                "            \"customfield_10603\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/customFieldOption/10602\",\n" +
                "                \"value\": \"Minor\",\n" +
                "                \"id\": \"10602\",\n" +
                "                \"disabled\": false\n" +
                "            },\n" +
                "            \"aggregatetimeestimate\": null,\n" +
                "            \"customfield_10604\": null,\n" +
                "            \"customfield_10605\": null,\n" +
                "            \"customfield_10606\": null,\n" +
                "            \"customfield_10607\": null,\n" +
                "            \"customfield_10609\": null,\n" +
                "            \"creator\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/user?username=licz\",\n" +
                "                \"name\": \"licz\",\n" +
                "                \"key\": \"licz\",\n" +
                "                \"emailAddress\": \"licz@pingpongx.com\",\n" +
                "                \"avatarUrls\": {\n" +
                "                    \"48x48\": \"http://jira.pingpongx.com/secure/useravatar?ownerId=licz&avatarId=11604\",\n" +
                "                    \"24x24\": \"http://jira.pingpongx.com/secure/useravatar?size=small&ownerId=licz&avatarId=11604\",\n" +
                "                    \"16x16\": \"http://jira.pingpongx.com/secure/useravatar?size=xsmall&ownerId=licz&avatarId=11604\",\n" +
                "                    \"32x32\": \"http://jira.pingpongx.com/secure/useravatar?size=medium&ownerId=licz&avatarId=11604\"\n" +
                "                },\n" +
                "                \"displayName\": \"李朝忠\",\n" +
                "                \"active\": true,\n" +
                "                \"timeZone\": \"Asia/Shanghai\"\n" +
                "            },\n" +
                "            \"subtasks\": [],\n" +
                "            \"reporter\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/user?username=licz\",\n" +
                "                \"name\": \"licz\",\n" +
                "                \"key\": \"licz\",\n" +
                "                \"emailAddress\": \"licz@pingpongx.com\",\n" +
                "                \"avatarUrls\": {\n" +
                "                    \"48x48\": \"http://jira.pingpongx.com/secure/useravatar?ownerId=licz&avatarId=11604\",\n" +
                "                    \"24x24\": \"http://jira.pingpongx.com/secure/useravatar?size=small&ownerId=licz&avatarId=11604\",\n" +
                "                    \"16x16\": \"http://jira.pingpongx.com/secure/useravatar?size=xsmall&ownerId=licz&avatarId=11604\",\n" +
                "                    \"32x32\": \"http://jira.pingpongx.com/secure/useravatar?size=medium&ownerId=licz&avatarId=11604\"\n" +
                "                },\n" +
                "                \"displayName\": \"李朝忠\",\n" +
                "                \"active\": true,\n" +
                "                \"timeZone\": \"Asia/Shanghai\"\n" +
                "            },\n" +
                "            \"aggregateprogress\": {\n" +
                "                \"progress\": 0,\n" +
                "                \"total\": 0\n" +
                "            },\n" +
                "            \"progress\": {\n" +
                "                \"progress\": 0,\n" +
                "                \"total\": 0\n" +
                "            },\n" +
                "            \"votes\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/issue/RECALL-4/votes\",\n" +
                "                \"votes\": 0,\n" +
                "                \"hasVoted\": false\n" +
                "            },\n" +
                "            \"worklog\": {\n" +
                "                \"startAt\": 0,\n" +
                "                \"maxResults\": 20,\n" +
                "                \"total\": 0,\n" +
                "                \"worklogs\": []\n" +
                "            },\n" +
                "            \"archivedby\": null,\n" +
                "            \"issuetype\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/issuetype/10500\",\n" +
                "                \"id\": \"10500\",\n" +
                "                \"description\": \"\",\n" +
                "                \"iconUrl\": \"http://jira.pingpongx.com/secure/viewavatar?size=xsmall&avatarId=10322&avatarType=issuetype\",\n" +
                "                \"name\": \"线上工单\",\n" +
                "                \"subtask\": false,\n" +
                "                \"avatarId\": 10322\n" +
                "            },\n" +
                "            \"timespent\": null,\n" +
                "            \"customfield_11240\": null,\n" +
                "            \"customfield_11241\": null,\n" +
                "            \"project\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/project/11305\",\n" +
                "                \"id\": \"11305\",\n" +
                "                \"key\": \"RECALL\",\n" +
                "                \"name\": \"流失运营项目\",\n" +
                "                \"projectTypeKey\": \"software\",\n" +
                "                \"avatarUrls\": {\n" +
                "                    \"48x48\": \"http://jira.pingpongx.com/secure/projectavatar?avatarId=10324\",\n" +
                "                    \"24x24\": \"http://jira.pingpongx.com/secure/projectavatar?size=small&avatarId=10324\",\n" +
                "                    \"16x16\": \"http://jira.pingpongx.com/secure/projectavatar?size=xsmall&avatarId=10324\",\n" +
                "                    \"32x32\": \"http://jira.pingpongx.com/secure/projectavatar?size=medium&avatarId=10324\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"customfield_11000\": null,\n" +
                "            \"customfield_11242\": null,\n" +
                "            \"customfield_11001\": null,\n" +
                "            \"customfield_11243\": null,\n" +
                "            \"customfield_11002\": null,\n" +
                "            \"customfield_11244\": null,\n" +
                "            \"aggregatetimespent\": null,\n" +
                "            \"customfield_10310\": null,\n" +
                "            \"customfield_11003\": null,\n" +
                "            \"customfield_11245\": null,\n" +
                "            \"customfield_11004\": null,\n" +
                "            \"customfield_11246\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/user?username=jianggm\",\n" +
                "                \"name\": \"jianggm\",\n" +
                "                \"key\": \"jianggm\",\n" +
                "                \"emailAddress\": \"jianggm@pingpongx.com\",\n" +
                "                \"avatarUrls\": {\n" +
                "                    \"48x48\": \"http://jira.pingpongx.com/secure/useravatar?avatarId=10122\",\n" +
                "                    \"24x24\": \"http://jira.pingpongx.com/secure/useravatar?size=small&avatarId=10122\",\n" +
                "                    \"16x16\": \"http://jira.pingpongx.com/secure/useravatar?size=xsmall&avatarId=10122\",\n" +
                "                    \"32x32\": \"http://jira.pingpongx.com/secure/useravatar?size=medium&avatarId=10122\"\n" +
                "                },\n" +
                "                \"displayName\": \"姜光明\",\n" +
                "                \"active\": true,\n" +
                "                \"timeZone\": \"Asia/Shanghai\"\n" +
                "            },\n" +
                "            \"customfield_11237\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/customFieldOption/11177\",\n" +
                "                \"value\": \"中价值\",\n" +
                "                \"id\": \"11177\",\n" +
                "                \"disabled\": false\n" +
                "            },\n" +
                "            \"customfield_10820\": null,\n" +
                "            \"customfield_11238\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/customFieldOption/11180\",\n" +
                "                \"value\": \"中价值\",\n" +
                "                \"id\": \"11180\",\n" +
                "                \"disabled\": false\n" +
                "            },\n" +
                "            \"customfield_10821\": null,\n" +
                "            \"customfield_10700\": null,\n" +
                "            \"customfield_11239\": null,\n" +
                "            \"customfield_10822\": null,\n" +
                "            \"customfield_10306\": null,\n" +
                "            \"customfield_10823\": null,\n" +
                "            \"customfield_10702\": \" \",\n" +
                "            \"customfield_10824\": null,\n" +
                "            \"customfield_10308\": null,\n" +
                "            \"resolutiondate\": null,\n" +
                "            \"customfield_10309\": null,\n" +
                "            \"workratio\": -1,\n" +
                "            \"watches\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/issue/RECALL-4/watchers\",\n" +
                "                \"watchCount\": 1,\n" +
                "                \"isWatching\": false\n" +
                "            },\n" +
                "            \"created\": \"2022-12-07T14:22:15.000+0800\",\n" +
                "            \"customfield_11230\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/customFieldOption/11333\",\n" +
                "                \"value\": \"ka\",\n" +
                "                \"id\": \"11333\",\n" +
                "                \"disabled\": false\n" +
                "            },\n" +
                "            \"customfield_11231\": null,\n" +
                "            \"customfield_11232\": \"一级\",\n" +
                "            \"customfield_11233\": null,\n" +
                "            \"customfield_11234\": 5,\n" +
                "            \"customfield_11235\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/customFieldOption/11173\",\n" +
                "                \"value\": \"活跃\",\n" +
                "                \"id\": \"11173\",\n" +
                "                \"disabled\": false\n" +
                "            },\n" +
                "            \"customfield_10301\": null,\n" +
                "            \"customfield_11236\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/customFieldOption/11182\",\n" +
                "                \"value\": \"高活\",\n" +
                "                \"id\": \"11182\",\n" +
                "                \"disabled\": false\n" +
                "            },\n" +
                "            \"customfield_11227\": \"orderid\",\n" +
                "            \"customfield_10810\": null,\n" +
                "            \"customfield_11228\": \"clientId\",\n" +
                "            \"customfield_10811\": null,\n" +
                "            \"customfield_11229\": {\n" +
                "                \"self\": \"http://jira.pingpongx.com/rest/api/2/customFieldOption/11329\",\n" +
                "                \"value\": \"P0\",\n" +
                "                \"id\": \"11329\",\n" +
                "                \"disabled\": false\n" +
                "            },\n" +
                "            \"customfield_10812\": null,\n" +
                "            \"customfield_10813\": null,\n" +
                "            \"customfield_10815\": null,\n" +
                "            \"customfield_10816\": null,\n" +
                "            \"customfield_10817\": null,\n" +
                "            \"customfield_10818\": null,\n" +
                "            \"customfield_10819\": null,\n" +
                "            \"updated\": \"2022-12-07T17:21:49.527+0800\",\n" +
                "            \"timeoriginalestimate\": null,\n" +
                "            \"description\": \"客户流失，测试，des\",\n" +
                "            \"customfield_11100\": \"{summaryBean=com.atlassian.jira.plugin.devstatus.rest.SummaryBean@54eeaeda[summary={pullrequest=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@43858f1c[overall=PullRequestOverallBean{stateCount=0, state='OPEN', details=PullRequestOverallDetails{openCount=0, mergedCount=0, declinedCount=0}},byInstanceType={}], build=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@39dabfba[overall=com.atlassian.jira.plugin.devstatus.summary.beans.BuildOverallBean@56483af2[failedBuildCount=0,successfulBuildCount=0,unknownBuildCount=0,count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}], review=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@6aa543e[overall=com.atlassian.jira.plugin.devstatus.summary.beans.ReviewsOverallBean@49b3da3[stateCount=0,state=<null>,dueDate=<null>,overDue=false,count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}], deployment-environment=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@68d065e5[overall=com.atlassian.jira.plugin.devstatus.summary.beans.DeploymentOverallBean@7fd84da7[topEnvironments=[],showProjects=false,successfulCount=0,count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}], repository=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@2b07edc6[overall=com.atlassian.jira.plugin.devstatus.summary.beans.CommitOverallBean@762cab20[count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}], branch=com.atlassian.jira.plugin.devstatus.rest.SummaryItemBean@1db8074d[overall=com.atlassian.jira.plugin.devstatus.summary.beans.BranchOverallBean@20b0d428[count=0,lastUpdated=<null>,lastUpdatedTimestamp=<null>],byInstanceType={}]},errors=[],configErrors=[]], devSummaryJson={\\\"cachedValue\\\":{\\\"errors\\\":[],\\\"configErrors\\\":[],\\\"summary\\\":{\\\"pullrequest\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null,\\\"stateCount\\\":0,\\\"state\\\":\\\"OPEN\\\",\\\"details\\\":{\\\"openCount\\\":0,\\\"mergedCount\\\":0,\\\"declinedCount\\\":0,\\\"total\\\":0},\\\"open\\\":true},\\\"byInstanceType\\\":{}},\\\"build\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null,\\\"failedBuildCount\\\":0,\\\"successfulBuildCount\\\":0,\\\"unknownBuildCount\\\":0},\\\"byInstanceType\\\":{}},\\\"review\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null,\\\"stateCount\\\":0,\\\"state\\\":null,\\\"dueDate\\\":null,\\\"overDue\\\":false,\\\"completed\\\":false},\\\"byInstanceType\\\":{}},\\\"deployment-environment\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null,\\\"topEnvironments\\\":[],\\\"showProjects\\\":false,\\\"successfulCount\\\":0},\\\"byInstanceType\\\":{}},\\\"repository\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null},\\\"byInstanceType\\\":{}},\\\"branch\\\":{\\\"overall\\\":{\\\"count\\\":0,\\\"lastUpdated\\\":null},\\\"byInstanceType\\\":{}}}},\\\"isStale\\\":false}}\",\n" +
                "            \"customfield_10410\": null,\n" +
                "            \"timetracking\": {},\n" +
                "            \"customfield_10800\": null,\n" +
                "            \"attachment\": [],\n" +
                "            \"customfield_10405\": null,\n" +
                "            \"customfield_10801\": null,\n" +
                "            \"customfield_10802\": null,\n" +
                "            \"customfield_10803\": null,\n" +
                "            \"customfield_10408\": null,\n" +
                "            \"customfield_10804\": null,\n" +
                "            \"customfield_10409\": null,\n" +
                "            \"customfield_10805\": null,\n" +
                "            \"customfield_10806\": null,\n" +
                "            \"customfield_10807\": null,\n" +
                "            \"customfield_10808\": null,\n" +
                "            \"customfield_10809\": null,\n" +
                "            \"summary\": \"客户流失，测试\",\n" +
                "            \"customfield_10000\": \"0|i0ehov:\",\n" +
                "            \"customfield_10001\": null,\n" +
                "            \"customfield_10002\": null,\n" +
                "            \"environment\": null,\n" +
                "            \"duedate\": null,\n" +
                "            \"comment\": {\n" +
                "                \"comments\": [],\n" +
                "                \"maxResults\": 0,\n" +
                "                \"total\": 0,\n" +
                "                \"startAt\": 0\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"changelog\": {\n" +
                "        \"id\": \"613391\",\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"field\": \"status\",\n" +
                "                \"fieldtype\": \"jira\",\n" +
                "                \"from\": \"1\",\n" +
                "                \"fromString\": \"待解决\",\n" +
                "                \"to\": \"10604\",\n" +
                "                \"toString\": \"解决中\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";
        Map<String, Object> jsonObject = PPConverter.toObject(message, new TypeReference<JSONObject>() {
        });
        System.out.println(jsonObject);
    }

}
