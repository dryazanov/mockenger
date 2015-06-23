package com.socialstartup.mockenger.core.util;

import com.socialstartup.mockenger.data.model.mock.request.RequestEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;

/**
 * Created by x079089 on 3/22/2015.
 */
public class CommonUtils {

    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static String getCheckSum(RequestEntity requestEntity) {
        if (requestEntity.getBody() != null && requestEntity.getBody().getValue() != null) {
            String bodyValue = requestEntity.getBody().getValue();
            if (!StringUtils.isEmpty(bodyValue)) {
                return DigestUtils.md5DigestAsHex(bodyValue.getBytes());
            }
        }
        return CommonUtils.generateCheckSum(requestEntity);
    }


    public static String generateCheckSum(RequestEntity requestEntity) {
        StringBuilder sb = new StringBuilder();
        if (requestEntity.getPath() != null && requestEntity.getPath().getValue() != null) {
            sb.append(requestEntity.getPath().getValue());
        }
        if (requestEntity.getParameters() != null && requestEntity.getParameters().getValues() != null) {
            sb.append(requestEntity.getParameters().getValues());
        }
        sb.append(requestEntity.getMethod());
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }


    public static String generateCheckSum(String ... args) {
        StringBuilder sb = new StringBuilder();
        for (String argument : args) {
            if (!StringUtils.isEmpty(argument)) {
                sb.append(argument);
            }
        }
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }


    /**
     * Checks if all the provided map are empty
     *
     * @param parameters
     * @return
     */
    public static boolean allEmpty(Map<String, String>... parameters) {
        for (Map<String, String> map : parameters) {
            if (!CollectionUtils.isEmpty(map)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks if all the provided maps are not empty
     *
     * @param parameters
     * @return
     */
    public static boolean allNotEmpty(Map<String, String>... parameters) {
        for (Map<String, String> map : parameters) {
            if (CollectionUtils.isEmpty(map)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Checks if map1 contains all the entities from map2
     *
     * @param map1
     * @param map2
     * @return
     */
    public static boolean containsAll(Map<String, String> map1, Map<String, String> map2) {
        return map1.entrySet().containsAll(map2.entrySet());
    }


    /**
     * Checks if two maps contain the same entities
     *
     * @param map1
     * @param map2
     * @return
     */
    public static boolean containsEqualEntries(Map<String, String> map1, Map<String, String> map2) {
        if (CollectionUtils.isEmpty(map1) || CollectionUtils.isEmpty(map2)) {
            return false;
        }
        for (Map.Entry<String, String> entry : map2.entrySet()) {
            if (!map1.containsKey(entry.getKey()) || !map1.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }
}
