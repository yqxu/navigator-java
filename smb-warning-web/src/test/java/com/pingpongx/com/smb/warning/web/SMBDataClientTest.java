package com.pingpongx.com.smb.warning.web;

import com.pingpongx.smb.open.sdk.core.client.DefaultSMBClient;
import com.pingpongx.smb.warning.web.client.SMBDataClient;
import org.junit.jupiter.api.Test;

public class SMBDataClientTest {

    SMBDataClient smbDataClient = new SMBDataClient(new DefaultSMBClient("https://dev2-smb-inner-gateway.pingpongx.com", "f32793b52cc0263ca0e4ff7ad71ab005", "4e7f5f9f0e3ec18daa4d0ce4de944320"));

    @Test
    public void test() {
        smbDataClient.queryCustomerInfo();
    }

}
