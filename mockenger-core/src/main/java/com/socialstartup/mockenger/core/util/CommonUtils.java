package com.socialstartup.mockenger.core.util;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.socialstartup.mockenger.data.model.persistent.mock.request.AbstractRequest;
import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Dmitry Ryazanov on 3/22/2015.
 */
public class CommonUtils {

    public static String generateUniqueId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String getCheckSum(final AbstractRequest abstractRequest) {
        if (abstractRequest.getBody() != null && !StringUtils.isEmpty(abstractRequest.getBody().getValue())) {
            return generateCheckSum(abstractRequest.getBody().getValue());
        }

        return CommonUtils.generateCheckSum(abstractRequest);
    }


    public static String generateCheckSum(final AbstractRequest abstractRequest) {
        final StringBuilder sb = new StringBuilder(abstractRequest.getPath().getValue());
        abstractRequest.getParameters().getValues().forEach(pair -> sb.append(pair.getKey()).append(pair.getValue()));
        return generateCheckSum(sb.append(abstractRequest.getMethod()).toString());
    }


    public static String generateCheckSum(final String ... args) {
        final StringBuilder sb = new StringBuilder();

        for (final String argument : args) {
            if (!StringUtils.isEmpty(argument)) {
                sb.append(argument);
            }
        }

        return generateCheckSum(sb.toString());
    }

    public static String generateCheckSum(final String source) {
        if (source == null) {
            return "";
        }
        return DigestUtils.md5DigestAsHex(source.getBytes(Charsets.UTF_8));
    }


    /**
     * Checks if all the provided map are empty
     *
     * @param parameters
     * @return
     */
    @SafeVarargs
    public static boolean allEmpty(final Map<String, String>... parameters) {
        for (final Map<String, String> map : parameters) {
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
    @SafeVarargs
    public static boolean allNotEmpty(final Map<String, String>... parameters) {
        for (final Map<String, String> map : parameters) {
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
    public static boolean allNotEmpty(final Set<Pair>... parameters) {
        for (final Set<Pair> set : parameters) {
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
    public static boolean containsAll(final Map<String, String> map1, final Map<String, String> map2) {
        return map1.entrySet().containsAll(map2.entrySet());
    }


    /**
     * Checks if set1 contains all the entities from set2
     *
     * @param set1
     * @param set2
     * @return
     */
    public static boolean containsAll(final Set<Pair> set1, final Set<Pair> set2) {
        return set1.containsAll(set2);
    }


    /**
     * Checks if two maps contain the same entities
     *
     * @param map1
     * @param map2
     * @return
     */
    public static boolean containsEqualEntries(final Map<String, String> map1, final Map<String, String> map2) {
        if (map1 == null || map2 == null) {
            return false;
        }
        return Maps.difference(map1, map2).areEqual();
    }

    /**
     * Checks if two maps contain the same entities
     *
     * @param set1
     * @param set2
     * @return
     */
    public static boolean containsEqualEntries(final Set<Pair> set1, final Set<Pair> set2) {
        if (set1 == null || set2 == null) {
            return false;
        }
        return Sets.difference(set1, set2).isEmpty();
    }
}
