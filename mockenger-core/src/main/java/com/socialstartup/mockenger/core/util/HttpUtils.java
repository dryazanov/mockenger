package com.socialstartup.mockenger.core.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by x079089 on 3/20/2015.
 */
public class HttpUtils {

    /**
     * Regex pattern to find in header's value all "," and ";" and spaces after them
     */
    private final static String DELIMITER_PATTERN = "(?<=[,;])\\s+";

    /**
     * Path matcher
     */
    private final static AntPathMatcher antPathMatcher = new AntPathMatcher();


    /**
     * Gets all the headers from request and returns them as Map<String, String>
     *
     * @param servletRequest
     * @param strictMatch true if you want to use headers as they are, false will set everything to lower case
     * @return
     */
    public static Map<String, String> getHeaders(HttpServletRequest servletRequest, boolean strictMatch) {
        String headerName;
        String headerValue;
        Map<String, String> requestHeaders = new HashMap<>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            headerName = headerNames.nextElement();
            if (strictMatch) {
                headerValue = servletRequest.getHeader(headerName);
            } else {
                headerName = headerName.toLowerCase();
                headerValue = servletRequest.getHeader(headerName).toLowerCase();
            }
            headerValue = headerValue.replaceAll(DELIMITER_PATTERN, "");
            requestHeaders.put(headerName, headerValue);
        }

        return requestHeaders;
    }

    /**
     * Gets all the query parameters and returns them as sorted Map<String, String>
     *
     * @param servletRequest
     * @return
     */
    public static Map<String, String> getParameterMap(HttpServletRequest servletRequest) {
        Map<String, String> parameterMap = new TreeMap<>();
        Enumeration<String> parameterNames = servletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameterMap.put(name, servletRequest.getParameter(name));
        }

        return (parameterMap.size() > 0 ? parameterMap : null);
    }

    /**
     * Gets request path
     *
     * @param servletRequest
     * @return
     */
    public static String getUrlPath(HttpServletRequest servletRequest) {
        return antPathMatcher.extractPathWithinPattern(
                (String) servletRequest.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE),
                (String) servletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
    }
}
