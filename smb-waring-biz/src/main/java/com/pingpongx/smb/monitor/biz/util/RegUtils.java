package com.pingpongx.smb.monitor.biz.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtils {

    /**
     * 按正则匹配字符串，提取 group1，有group0的，但包含了额外的字符串
     *
     * @param s
     * @param reg
     * @return
     */
    public static String extractStringByReg(String s, String reg) {
        Pattern pattern = Pattern.compile(reg, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public static void main(String[] args) {
        String s  = "Error {\n" +
                "  message='Timeout 20000ms exceeded.\n" +
                "=========================== logs ===========================\n" +
                "waiting for getByPlaceholder(\"邮箱地址1\")\n" +
                "============================================================\n" +
                "  name='TimeoutError\n" +
                "  stack='TimeoutError: Timeout 20000ms exceeded.\n" +
                "=========================== logs ===========================\n" +
                "waiting for getByPlaceholder(\"邮箱地址1\")\n" +
                "============================================================\n" +
                "    at ProgressController.run (/private/var/folders/8t/1rsccqy177b942qq92bdslxn38735j/T/playwright-java-1243377130121288398/package/lib/server/progress.js:88:26)\n" +
                "    at Frame.click (/private/var/folders/8t/1rsccqy177b942qq92bdslxn38735j/T/playwright-java-1243377130121288398/package/lib/server/frames.js:1019:23)\n" +
                "    at FrameDispatcher.click (/private/var/folders/8t/1rsccqy177b942qq92bdslxn38735j/T/playwright-java-1243377130121288398/package/lib/server/dispatchers/frameDispatcher.js:149:30)\n" +
                "    at DispatcherConnection.dispatch (/private/var/folders/8t/1rsccqy177b942qq92bdslxn38735j/T/playwright-java-1243377130121288398/package/lib/server/dispatchers/dispatcher.js:319:46)\n" +
                "}";
        System.out.println(extractStringByReg(s, "logs =+(.*?)="));
    }
}
