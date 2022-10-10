package com.pingpongx.smb.warning.biz.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.pingpongx.smb.warning.api.dto.JiraDTO;
import com.pingpongx.smb.warning.api.dto.JiraItemDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class JiraUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    private final static Configuration configuration = Configuration.defaultConfiguration()
            .mappingProvider(new JacksonMappingProvider(objectMapper))
            .jsonProvider(new JacksonJsonProvider(objectMapper))
            .setOptions(Option.SUPPRESS_EXCEPTIONS, Option.DEFAULT_PATH_LEAF_TO_NULL);

    public static JiraDTO parseJiraDTO(String data) {
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
