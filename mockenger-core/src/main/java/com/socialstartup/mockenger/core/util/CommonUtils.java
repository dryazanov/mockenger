package com.socialstartup.mockenger.core.util;

import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by x079089 on 3/22/2015.
 */
public class CommonUtils {

    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getCheckSum(AbstractRequest abstractRequest) {
        if (abstractRequest.getBody() != null && abstractRequest.getBody().getValue() != null) {
            String bodyValue = abstractRequest.getBody().getValue();
            if (!StringUtils.isEmpty(bodyValue)) {
                return DigestUtils.md5DigestAsHex(bodyValue.getBytes());
            }
        }
        return CommonUtils.generateCheckSum(abstractRequest);
    }


    public static String generateCheckSum(AbstractRequest abstractRequest) {
        StringBuilder sb = new StringBuilder();
        if (abstractRequest.getPath() != null && abstractRequest.getPath().getValue() != null) {
            sb.append(abstractRequest.getPath().getValue());
        }
        if (abstractRequest.getParameters() != null && abstractRequest.getParameters().getValues() != null) {
            for (Pair pair : abstractRequest.getParameters().getValues()) {
                sb.append(pair.getKey()).append(pair.getValue());
            }
        }
        sb.append(abstractRequest.getMethod());
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
     * Checks if all the provided maps are not empty
     *
     * @param parameters
     * @return
     */
    public static boolean allNotEmpty(Set<Pair>... parameters) {
        for (Set<Pair> set : parameters) {
            if (CollectionUtils.isEmpty(set)) {
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
     * Checks if set1 contains all the entities from set2
     *
     * @param set1
     * @param set2
     * @return
     */
    public static boolean containsAll(Set<Pair> set1, Set<Pair> set2) {
        return set1.containsAll(set2);
    }


    /**
     * Checks if two maps contain the same entities
     *
     * @param map1
     * @param map2
     * @return
     */
    public static boolean containsEqualEntries(Map<String, String> map1, Map<String, String> map2) {
        if (map1 == null && map2 == null) {
            return true;
        }
        if ((map1 == null && map2 != null) || map1 != null && map2 == null || map1.size() != map2.size()) {
            return false;
        }
        for (Map.Entry<String, String> entry : map2.entrySet()) {
            if (!map1.containsKey(entry.getKey()) || !map1.get(entry.getKey()).equals(entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if two maps contain the same entities
     *
     * @param set1
     * @param set2
     * @return
     */
    public static boolean containsEqualEntries(Set<Pair> set1, Set<Pair> set2) {
        if (set1 == null && set2 == null) {
            return true;
        }
        if ((set1 == null && set2 != null) || set1 != null && set2 == null || set1.size() != set2.size()) {
            return false;
        }
        for (Pair pair : set1) {
            if (!set2.contains(pair)) {
                return false;
            }
        }
        return true;
    }
}
