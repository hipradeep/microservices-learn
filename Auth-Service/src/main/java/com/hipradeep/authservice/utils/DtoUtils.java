package com.hipradeep.authservice.utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
public class DtoUtils {

    /**
     * Copies the properties of the source object to the target object.
     *
     * @param source the source object
     * @param target the target object
     */
    public static void copyProperties(Object source, Object target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target must not be null");
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    /**
     * Generic method to copy properties from source object to a new target object.
     *
     * @param source the source object
     * @param targetType the target class type
     * @return the target object with properties copied from source
     */
    public static <S, T> T copyProperties(S source, Class<T> targetType) {
        if (source == null) {
            throw new IllegalArgumentException("Source must not be null");
        }

        T target;
        try {
            target = targetType.getDeclaredConstructor().newInstance();
            org.springframework.beans.BeanUtils.copyProperties(source, target);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create target instance or copy properties", e);
        }
        return target;
    }

    /**
     * Generic method to copy properties from a list of source objects to a list of target objects.
     *
     * @param sourceList the source object list
     * @param targetType the target class type
     * @return a list of target objects with properties copied from source objects
     */
    public static <S, T> List<T> copyListProperties(List<S> sourceList, Class<T> targetType) {
        if (sourceList == null) {
            throw new IllegalArgumentException("Source list must not be null");
        }

        return sourceList.stream()
                .map(source -> copyProperties(source, targetType))
                .collect(Collectors.toList());
    }
}
