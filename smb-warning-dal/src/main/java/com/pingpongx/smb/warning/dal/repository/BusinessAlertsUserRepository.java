package com.pingpongx.smb.warning.dal.repository;

import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsUser;
import com.pingpongx.smb.warning.dal.dataobject.BusinessAlertsUserMap;
import com.pingpongx.smb.warning.dal.mapper.BusinessAlertsUserMapper;
import com.pingpongx.business.dal.core.BaseRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 开发负责人信息表 Repository
 * </p>
 *
 * @author PingPong Code Generator
 * @since 2022-09-07
 */
@Repository
public class BusinessAlertsUserRepository extends BaseRepository<BusinessAlertsUserMapper, BusinessAlertsUser> {

    public List<BusinessAlertsUserMap> findUserByAppNames(List<String> appNames){
        return baseMapper.findUserByAppNames(appNames);
    }

    public List<BusinessAlertsUserMap> findAllBusinessAlertUsers(){
        return baseMapper.findAllBusinessAlertUsers();
    }
}
