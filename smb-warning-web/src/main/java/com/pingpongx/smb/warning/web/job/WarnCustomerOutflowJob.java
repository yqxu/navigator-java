package com.pingpongx.smb.warning.web.job;

import com.pingpongx.flowmore.cloud.base.server.annotation.XTraceId;
import com.pingpongx.job.core.biz.model.ReturnT;
import com.pingpongx.job.core.handler.IJobHandler;
import com.pingpongx.job.core.handler.annotation.JobHandler;
import com.pingpongx.smb.warning.web.service.CustomerOutflowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@JobHandler("WarnCustomerOutflowJob")
@Component
public class WarnCustomerOutflowJob extends IJobHandler {

    @Autowired
    private CustomerOutflowService customerOutflowService;

    @Override
    @XTraceId
    public ReturnT<String> execute(String s) {
        try {
            log.info("客户流失预警");
            customerOutflowService.preWarnOfOutflow();
            log.info("客户流失预警成功");
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            log.error("客户流失预警异常", e);
            return ReturnT.FAIL;
        }
    }
}
