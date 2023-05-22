package com.pingpongx.smb.monitor.biz.exception;

public class ScriptException extends RuntimeException {

    public ScriptException() {
        super("元素识别异常，脚本可能需要更新");
    }

    public ScriptException(String msg) {
        super(msg);
    }

}
