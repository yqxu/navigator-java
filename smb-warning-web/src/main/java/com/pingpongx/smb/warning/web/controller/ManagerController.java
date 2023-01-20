package com.pingpongx.smb.warning.web.controller;

import com.pingpongx.business.dal.core.BaseDO;
import com.pingpongx.flowmore.cloud.base.server.annotation.NoAuth;
import com.pingpongx.smb.warning.biz.alert.event.AlertReceived;
import com.pingpongx.smb.warning.biz.alert.model.ThirdPartAlert;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsApp;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsToUser;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsUser;
import com.pingpongx.smb.warning.dal.repository.BusinessAlertsAppRepository;
import com.pingpongx.smb.warning.dal.repository.BusinessAlertsToUserRepository;
import com.pingpongx.smb.warning.dal.repository.BusinessAlertsUserRepository;
import com.pingpongx.smb.warning.web.parser.AlertParser;
import com.pingpongx.smb.warning.web.parser.ParserFactory;
import com.pingpongx.smb.warning.web.vo.UserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: xuyq
 * @Date: 2022/09/06/7:10 下午
 * @Description: 管理态接口
 * @Version:
 */

@Slf4j
@RestController
@RequestMapping("/v2/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final ParserFactory parserFactory;
    private final ApplicationContext context;
    private final BusinessAlertsToUserRepository relationRepository;
    private final BusinessAlertsUserRepository userRepository;
    private final BusinessAlertsAppRepository appRepository;

    @PostMapping("/{app}/bind")
    @NoAuth(isPack = false)
    public BusinessAlertsToUser bind(@PathVariable("app") String app, @RequestBody BusinessAlertsToUser relation) {
        relation.setAppName(app);
        relationRepository.save(relation);
        return relation;
    }


    @PostMapping("/{app}/unbind")
    @NoAuth(isPack = false)
    public boolean unBind(@PathVariable("app") String app, @RequestBody String userId) {
        boolean updated = relationRepository.lambdaUpdate().eq(BusinessAlertsToUser::getUserId,userId).eq(BusinessAlertsToUser::getAppName,app).set(BaseDO::getFlag,0).update();
        return updated;
    }

    @PostMapping("/user/saveOrUpdate")
    @NoAuth(isPack = false)
    public boolean saveOrUpdateUser( @RequestBody BusinessAlertsUser user) {
        return userRepository.saveOrUpdate(user);
    }
    @PostMapping("/app/addApp")
    @NoAuth(isPack = false)
    public boolean saveOrUpdateApp( @RequestBody BusinessAlertsApp app) {
        return appRepository.saveOrUpdate(app);
    }
}
