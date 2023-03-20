package com.pingpongx.smb.export.module;


import com.alibaba.fastjson2.JSON;
import com.pingpongx.smb.export.module.persistance.Range;

public class ConfiguredRangeRule extends ConfiguredRule<Range> {

    @Override
    public Range expected() {
        return JSON.parseObject(expected,Range.class);
    }

}
