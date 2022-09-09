package com.pingpongx.smb.warning.web.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.pingpongx.flowmore.cloud.base.server.annotation.NoAuth;
import com.pingpongx.smb.warning.api.dto.JiraDTO;
import com.pingpongx.smb.warning.api.dto.JiraItemDTO;
import com.pingpongx.smb.warning.web.helper.BusinessAlertHelper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/10:23 上午
 * @Description: 回调请求
 * @Version:
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/smb/webhook")
public class WebhookController {

    private final BusinessAlertHelper businessAlertHelper;

    private final static ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    private final static Configuration configuration = Configuration.defaultConfiguration()
        .mappingProvider(new JacksonMappingProvider(objectMapper))
        .jsonProvider(new JacksonJsonProvider(objectMapper))
        .setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

    @PostMapping("/jirahook")
    @NoAuth(isPack = false)
    public void jiraHook(@RequestBody String message) {
        log.info("jiraHook message:[{}]", message);
        try {
            JiraDTO jiraDTO = parseJiraDTO(message);
            businessAlertHelper.sendDingTalkMarkDown(jiraDTO);
        } catch (Exception e) {
            log.warn("[jiraHook] error, message [{}]", message, e);
        }
    }

    private static JiraDTO parseJiraDTO(String data) {
        DocumentContext documentContext = JsonPath.using(configuration).parse(data);
        String url = "http://jira.pingpongx.com/browse/" + documentContext.read("$.issue.key");
        List<JiraItemDTO> itemDTOS = documentContext.read("$.changelog.items", new TypeRef<List<JiraItemDTO>>() {
        });
        Map<String, String> itemDict = Optional.ofNullable(itemDTOS).orElseGet(Lists::newArrayList).stream()
            .collect(Collectors.toMap(JiraItemDTO::getField, JiraItemDTO::getToString, (o, n) -> n));
        return JiraDTO.builder().url(url)
            .status(documentContext.read("$.issue.fields.status.name"))
            .issueType(documentContext.read("$.issue.fields.issuetype.name"))
            .summary(documentContext.read("$.issue.fields.summary"))
            .assignee(documentContext.read("$.issue.fields.assignee.emailAddress"))
            .reporter(documentContext.read("$.issue.fields.reporter.emailAddress"))
            .worker(documentContext.read("$.issue.fields.customfield_10700.emailAddress"))
            .reason(itemDict.get("报警引发原因"))
            .action(itemDict.get("解决方案"))
            .build();
    }
}
