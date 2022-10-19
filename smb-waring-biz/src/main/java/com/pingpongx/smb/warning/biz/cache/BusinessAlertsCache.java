package com.pingpongx.smb.warning.biz.cache;

import com.alibaba.fastjson.JSONObject;
import com.pingpongx.smb.warning.api.dto.DingDReceiverDTO;
import com.pingpongx.smb.warning.biz.util.ConvertUtil;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsUser;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsUserMap;
import com.pingpongx.smb.warning.dal.repository.BusinessAlertsUserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Auther: jiangkun
 * @Date: 2022/06/21/4:32 下午
 * @Description: 业务告警缓存
 * @Version:
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class BusinessAlertsCache {

    private final Map<String, List<BusinessAlertsUserMap>> businessAlertUserMap = new HashMap<>();

    private final Map<String, BusinessAlertsUser> leaderAlertMap = new HashMap<>();

    private final BusinessAlertsUserRepository businessAlertsUserRepository;

    @Value("${business.alert.dingding.default.user:}")
    private String defaultReceiverConfig;

    /**
     * 获取默认告警负责人
     * @return 告警负责人
     */
    public DingDReceiverDTO defaultReceiver() {
        DingDReceiverDTO defaultReceiver;
        try {
            defaultReceiver = JSONObject.parseObject(defaultReceiverConfig, DingDReceiverDTO.class);
        }catch(Exception ex){
            defaultReceiver = DingDReceiverDTO.getDefaultUser();
            log.warn("解析配置中心默认告警异常通知人错误", ex);
        }
        return defaultReceiver;
    }

    @PostConstruct
    public void initAlertUser() {
        List<BusinessAlertsUser> leaderUsers = businessAlertsUserRepository.lambdaQuery().eq(BusinessAlertsUser::getTeamLeaderId, "").list();
        if (CollectionUtils.isNotEmpty(leaderUsers)) {
            for (BusinessAlertsUser alertsUser : leaderUsers) {
                leaderAlertMap.put(alertsUser.getUserId(), alertsUser);
            }
        }
        List<BusinessAlertsUserMap> allBusinessAlertUsers = businessAlertsUserRepository.findAllBusinessAlertUsers();
        if (CollectionUtils.isEmpty(allBusinessAlertUsers)) {
            return;
        }
        for (BusinessAlertsUserMap alertsUser : allBusinessAlertUsers) {
            List<BusinessAlertsUserMap> list = businessAlertUserMap.get(alertsUser.getAppName());
            if (list == null){
                list = new ArrayList<>();
                businessAlertUserMap.put(alertsUser.getAppName(),list);
            }
            list.add(alertsUser);
        }
        log.info("BusinessAlertsCache.initAlertUser[业务告警应用负责人缓存信息:{}]", JSONObject.toJSONString(businessAlertUserMap));
    }

    public synchronized void allCacheRefresh() {
        //删除缓存数据
        leaderAlertMap.clear();
        businessAlertUserMap.clear();
        this.initAlertUser();
    }

    public synchronized void cacheRefreshByAppName(List<String> appNames) {
        List<BusinessAlertsUserMap> userByAppNames = businessAlertsUserRepository.findUserByAppNames(appNames);
        if (CollectionUtils.isEmpty(userByAppNames)) {
            return;
        }
        Map<String, List<BusinessAlertsUserMap>> filterByAppNameMap = userByAppNames.stream().collect(Collectors.groupingBy(BusinessAlertsUserMap::getAppName));
        for (String appName : appNames) {
            if (filterByAppNameMap.containsKey(appName)) {
                // 更新缓存中负责人信息
                businessAlertUserMap.put(appName, filterByAppNameMap.get(appName));
            }
        }
    }

    /**
     * 根据appName 查询应用负责人
     *
     * @param appName 应用名称
     * @return List<BusinessAlertsUserMap> 应用负责人
     */
    public List<BusinessAlertsUserMap> findByAppName(String appName) {
        List<BusinessAlertsUserMap> alertUsers = businessAlertUserMap.get(appName);
        if (businessAlertUserMap.isEmpty() || CollectionUtils.isEmpty(alertUsers)) {
            log.error("未设置应用告警负责人,请及时维护应用告警负责人！");
            return null;
        }
        List<BusinessAlertsUserMap> alertsUserMapList = new ArrayList<>(alertUsers);
        // 通知小组组长
        List<BusinessAlertsUserMap> leaders = alertUsers.stream().map(BusinessAlertsUserMap::getTeamLeaderId).distinct().filter(leaderAlertMap::containsKey)
            .map(item -> {
                BusinessAlertsUserMap alertsUser = ConvertUtil.convert(leaderAlertMap.get(item), BusinessAlertsUserMap.class);
                alertsUser.setAppName(appName);
                return alertsUser;
            }).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(leaders)) {
            alertsUserMapList.addAll(leaders);
        }
        return alertsUserMapList;
    }
}
