package com.pingpongx.smb.monitor.biz.exception;

public class LoginException extends RuntimeException {

    public LoginException() {
        super("登录失败，请检查");
    }
}
