package com.pingpongx.com.smb.warning.web;

import com.pingpongx.smb.export.globle.Engine;
import com.pingpongx.smb.export.module.ConfiguredStrRule;
import com.pingpongx.smb.export.module.PipelineContext;
import com.pingpongx.smb.export.spi.FunctionalHandler;
import com.pingpongx.smb.rule.routers.operatiors.StringContains;
import com.pingpongx.smb.warning.biz.alert.model.SlsAlert;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EngineTest {
    Engine engine = Engine.newInstance();
    Engine debugEngine = Engine.newDebugAbleInstance();

    @Test
    public void test() {
        SlsAlert alert = new SlsAlert();
        alert.setAppName("test");
        alert.setContent("tttttttttteeeeeeetestsssssss");
        ConfiguredStrRule rule1 = new ConfiguredStrRule();
        rule1.setType(alert.getClass().getSimpleName());
        rule1.setAttr("content");
        rule1.setExpected("test");
        rule1.setOperation(StringContains.getInstance(rule1.getType(), rule1.getAttr()));
        rule1.setDebugHandlerCodes(Stream.of("ShowDeviation").collect(Collectors.toList()));
        FunctionalHandler functionalHandler = new FunctionalHandler() {
            @Override
            public List<String> tags() {
                return null;
            }

            @Override
            public Object applyData(Object data) {
                return "";
            }

            @Override
            public Object getIdentify() {
                return "test";
            }
        };
        engine.put(rule1, functionalHandler);
        debugEngine.put(rule1, functionalHandler);
        debugEngine.putDebug(rule1);
        engine.match(alert);
        debugEngine.match(alert);
        debugEngine.debug(alert);
    }


}
