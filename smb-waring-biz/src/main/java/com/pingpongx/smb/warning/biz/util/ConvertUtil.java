package com.pingpongx.smb.warning.biz.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

@Slf4j
public final class ConvertUtil {

    private ConvertUtil() {
    }

    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target);
            log.debug("convert {}:{}to {}:{}", source.toString(), source.hashCode(), target.toString(), target.hashCode());
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("error happened when converting", e);
        }
        return null;
    }

    public static <T> List<T> convert(List source, Class<T> clazz) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        ArrayList<T> result = new ArrayList<>(source.size());
        if (CollectionUtils.isEmpty(source)) {
            return result;
        }
        for (Object s : source) {
            result.add(convert(s, clazz));
        }
        return result;
    }
}
