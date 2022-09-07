package com.pingpongx.smb.warning.biz.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author Nick 2019/4/1 09:31
 */
@Slf4j
public class BeanUtils extends org.springframework.beans.BeanUtils {

    public static <T> T convert(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.newInstance();
            copyProperties(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("BeanUtils.convert ERROR", e);
        }
        return null;
    }

    public static <T> List<T> convert(List source, Class<T> clazz) {
        ArrayList<T> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(source)) {
            return result;
        }
        for (Object s : source) {
            result.add(convert(s, clazz));
        }
        return result;
    }

    public static void populate(Object bean, Map<String, ? extends Object> properties) {
        // Do nothing unless both arguments have been specified
        if ((bean == null) || (properties == null)) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("BeanUtils.populate(" + bean + ", " +
                    properties + ")");
        }

        // Loop through the property name/value pairs to be set
        for (final Map.Entry<String, ? extends Object> entry : properties.entrySet()) {
            // Identify the property name and value(s) to be assigned
            final String name = entry.getKey();
            if (name == null) {
                continue;
            }
            // Perform the assignment for this property
            Field field = ReflectionUtils.findField(bean.getClass(), name);
            if (field == null) {
                continue;
            }
            ReflectionUtils.makeAccessible(field);
            ReflectionUtils.setField(field, bean, entry.getValue());
        }
    }
}
