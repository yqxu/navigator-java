package com.pingpongx.smb.warning.dal.mapper;

import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsUser;
import com.pingpongx.business.dal.core.DefaultMapper;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsUserMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 开发负责人信息表 Mapper 接口
 * </p>
 *
 * @author PingPong Code Generator
 * @since 2022-09-07
 */
public interface BusinessAlertsUserMapper extends DefaultMapper<BusinessAlertsUser> {

    @Select("select a.appName,u.userId,u.teamLeaderId,u.name,u.callingPrefix,u.phone,u.email,u.domainAccount from business_alerts_to_user a left join business_alerts_user u on a.userId = u.userId where a.flag = 1 and u.flag =1")
    List<BusinessAlertsUserMap> findAllBusinessAlertUsers();

    List<BusinessAlertsUserMap> findUserByAppNames(@Param("appNames") List<String> appNames);
}
