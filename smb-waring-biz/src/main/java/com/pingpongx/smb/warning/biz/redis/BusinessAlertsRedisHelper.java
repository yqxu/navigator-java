package com.pingpongx.smb.warning.biz.redis;

import com.alibaba.fastjson.JSONObject;
import com.pingpongx.smb.warning.biz.constants.BusinessAlertProperty;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Auther: jiangkun
 * @Date: 2022/09/07/4:53 下午
 * @Description:
 * @Version:
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessAlertsRedisHelper {
    private final StringRedisTemplate jiraRedisTemplate;
    private final BusinessAlertProperty property;

    /**
     * 缓存时间8天
     */
    private static final Long CACHE_TIME = 8 * 24L;
    private static final String CACHE_KEY = "BusinessAlert:JIRA:";
    private static final String CACHE_TIME_OUT_KEY = "BusinessAlert:JIRA:timeOutException";

    public Boolean hasCreateJiraByCache(String appName, String content) {
        if (StringUtils.isEmpty(content)) {
            return Boolean.FALSE;
        }
        String key = CACHE_KEY + appName;
        try {
            String cacheVar = DigestUtils.md5Hex(content);
            Boolean isExist = jiraRedisTemplate.opsForSet().isMember(key, cacheVar);
            if (null == isExist || !isExist) {
                jiraRedisTemplate.opsForSet().add(key, cacheVar);
                jiraRedisTemplate.expire(key, CACHE_TIME, TimeUnit.HOURS);
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.warn("查询告警是否存在缓存数据失败", ex);
            return Boolean.FALSE;
        }
    }

    /**
     * 判断是否需要进行超时告警
     *
     * @param appName 应用名
     * @param content 告警内容
     * @return true/false
     */
    public boolean needSendDingDing(String appName, String content) {
        long currentTime = Instant.now().toEpochMilli() / 1000;
        // 时间间隙
        long timeInter = Long.parseLong(property.getTimeOutAlertTimeInterval()[1]) * 60;
        // 开始时间
        long startTime = currentTime - timeInter;
        String redisKey = CACHE_TIME_OUT_KEY + ":" + appName + ":" + DigestUtils.md5Hex(content);
        jiraRedisTemplate.opsForList().leftPush(redisKey, String.valueOf(currentTime));
        jiraRedisTemplate.expire(redisKey, timeInter, TimeUnit.SECONDS);
        // 目前可用数据集合
        int nowSize = Objects.requireNonNull(jiraRedisTemplate.opsForList().range(redisKey, 0, -1)).size();
        if (nowSize < Long.parseLong(property.getTimeOutAlertTimeInterval()[0])) {
            return true;
        }
        while (true) {
            String s = jiraRedisTemplate.opsForList().rightPop(redisKey);
            if (StringUtils.isEmpty(s)) {
                break;
            }
            long r = Long.parseLong(s);
            if (r > startTime) {
                break;
            }
        }
        int size = 1;
        List<String> range = jiraRedisTemplate.opsForList().range(redisKey, 0, -1);
        log.info("超时请求缓存,5分钟内请求的数据集合:{}", JSONObject.toJSONString(range));
        if (CollectionUtils.isEmpty(range)) {
            return true;
        }
        size += range.size();
        return size < Long.parseLong(property.getTimeOutAlertTimeInterval()[0]);
    }
}
