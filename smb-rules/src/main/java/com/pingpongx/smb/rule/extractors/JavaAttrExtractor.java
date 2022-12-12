package com.pingpongx.smb.rule.extractors;

import com.pingpongx.smb.common.utils.ReflectionUtils;
import com.pingpongx.smb.export.spi.AttrExtractor;

import java.lang.reflect.Field;

public class JavaAttrExtractor implements AttrExtractor {

    @Override
    public Object getAttr(Object data,String attrPath) {
        try {
            Field field = data.getClass().getDeclaredField(attrPath);
            field.setAccessible(true);
            Object attrVal = ReflectionUtils.getField(field,data);
            return attrVal;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
