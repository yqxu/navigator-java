package com.pingpongx.smb.warning.web.client;

import com.github.benmanes.caffeine.cache.Cache;
import com.pingpongx.flowmore.cloud.base.server.utils.CacheUtils;
import com.pingpongx.smb.organization.api.PPUserFeign;
import com.pingpongx.smb.organization.common.resp.PPUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PPUserClient {

    @Value("${customer.churn.pre.warn.orgIds:655319364,751205249}")
    private List<Long> orgIds;

    private static final String cacheKey = "customer.churn.pre.warn.orgIds";
    private static final Cache<String, Object> cache = CacheUtils.cacheManager(cacheKey, 60);

    private final PPUserFeign ppUserFeign;

    public List<PPUser> queryUserInfo() {
        Object obj = cache.getIfPresent(cacheKey);
        if (obj == null) {
            List<PPUser> ppUsers = ppUserFeign.queryUserInfoByOrgIds(orgIds);
            cache.put(cacheKey, ppUsers);
            return ppUsers;
        } else {
            return (List<PPUser>) obj;
        }
    }

}
