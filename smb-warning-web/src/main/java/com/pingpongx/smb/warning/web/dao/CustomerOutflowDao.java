package com.pingpongx.smb.warning.web.dao;

import com.pingpongx.flowmore.cloud.base.server.utils.TransferUtils;
import com.pingpongx.smb.warning.dal.dataobject.CustomerOutflowOrder;
import com.pingpongx.smb.warning.dal.dataobject.JiraInfo;
import com.pingpongx.smb.warning.dal.repository.CustomerOutflowOrderRepository;
import com.pingpongx.smb.warning.dal.repository.JiraInfoRepository;
import com.pingpongx.smb.warning.web.module.CustomerOrderInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerOutflowDao {

    private final CustomerOutflowOrderRepository customerOutflowOrderRepository;

    private final JiraInfoRepository jiraInfoRepository;

    @Transactional(rollbackFor = Exception.class)
    public void save(CustomerOrderInfo customerOrderInfo) {
        customerOutflowOrderRepository.save(TransferUtils.transfer(customerOrderInfo, CustomerOutflowOrder::new));
        jiraInfoRepository.save(TransferUtils.transfer(customerOrderInfo, JiraInfo::new));
    }
}
