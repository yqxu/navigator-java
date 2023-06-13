package com.pingpongx.smb.monitor.biz.util;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.ttl.threadpool.agent.internal.javassist.*;
import com.microsoft.playwright.Locator;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayWrightUtils {

    static {
        playWrightAdvance();
    }

    public static void initUtil() {

    }

    public static boolean waitElementExist(Locator locator, long ms) {
        long hasWait = 0L;
        Locator.WaitForOptions waitForOptions = new Locator.WaitForOptions();
        waitForOptions.setTimeout(500);
        while (!locator.isVisible() && hasWait <= ms) {
            try {
                locator.waitFor(waitForOptions);
            } catch (Exception ignore) {
            }
            log.info("waitElementExist, locator:{}", locator);
            hasWait = hasWait + 500;
        }
        return hasWait <= ms;
    }

    public static void playWrightAdvance() {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
        String clsNameLocatorImpl = "com.microsoft.playwright.impl.LocatorImpl";
        String clsNameClickOptions = Locator.ClickOptions.class.getName();
        String clsNameFillOptions = Locator.FillOptions.class.getName();
        String clsNameString = String.class.getName();

        CtClass locatorImplClass = null;
        CtClass clickOptionsClass = null;
        CtClass fillOptionsClass = null;
        CtClass stringClass = null;
        try {

            locatorImplClass = classPool.get(clsNameLocatorImpl);
            clickOptionsClass = classPool.get(clsNameClickOptions);
            fillOptionsClass = classPool.get(clsNameFillOptions);
            stringClass = classPool.get(clsNameString);

            // 在 LocatorImpl类的click方法前增加日志打印
            CtMethod ctMethodClick = locatorImplClass.getDeclaredMethod("click", new CtClass[]{clickOptionsClass});
            ctMethodClick.insertBefore("org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(com.microsoft.playwright.impl.LocatorImpl.class);log.info(\"click: \" + selector);");

            // 在 LocatorImpl类的fill方法前增加日志打印
            CtMethod ctMethodFill = locatorImplClass.getDeclaredMethod("fill", new CtClass[]{stringClass, fillOptionsClass});
            ctMethodFill.insertBefore("org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(com.microsoft.playwright.impl.LocatorImpl.class);log.info(\"fill: \" + selector);");


            locatorImplClass.toClass();
            // locatorImplClass.writeFile();

        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        } finally {
            // CtClass对象非常多的时候，ClassPool将会消耗内存巨大，为了避免，将其从ClassPool移除
            if (locatorImplClass != null) {
                locatorImplClass.detach();
            }
            if (clickOptionsClass != null) {
                clickOptionsClass.detach();
            }
            if (fillOptionsClass != null) {
                fillOptionsClass.detach();
            }
            if (stringClass != null) {
                stringClass.detach();
            }
        }
    }
}
