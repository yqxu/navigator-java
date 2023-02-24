package com.pingpongx.smb.monitor.biz.util;

import cn.hutool.core.thread.ThreadUtil;
import com.microsoft.playwright.Locator;

public class PlayWrightUtils {

    public static boolean waitElementExist(Locator locator, long ms) {
        long hasWait = 0L;
        while (!locator.isVisible() && hasWait <= ms) {
            ThreadUtil.sleep(120);
            hasWait = hasWait + 1000;
        }
        return hasWait <= ms;
    }
}
