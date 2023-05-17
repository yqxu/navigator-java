package com.pingpongx.smb.monitor.biz.exception;

public class LoginException extends RuntimeException {

    public LoginException() {
        super("可能是某个界面元素未识别，导致登录失败，请检查");
    }

    public LoginException(String msg) {
        super(msg);
    }
}
