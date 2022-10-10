package com.pingpongx.com.smb.warning.web;

import com.pingpongx.flowmore.cloud.base.server.app.BaseApplicationRun;
import com.pingpongx.flowmore.cloud.base.server.app.BaseTest;
import com.pingpongx.smb.warning.web.SmbWarningApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {SmbWarningApplication.class, BaseApplicationRun.class})
//@Transactional
public abstract class BaseServerTest extends BaseTest {
}
