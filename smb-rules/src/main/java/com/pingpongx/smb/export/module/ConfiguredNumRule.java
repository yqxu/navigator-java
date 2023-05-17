package com.pingpongx.smb.export.module;


import java.math.BigDecimal;

public class ConfiguredNumRule extends ConfiguredRule<Number> {


    @Override
    public Number expected() {
        return new BigDecimal(this.expected);
    }
}
