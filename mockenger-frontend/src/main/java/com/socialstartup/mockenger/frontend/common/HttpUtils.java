package com.socialstartup.mockenger.frontend.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

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
            headerValue = headerValue.replaceAll("(?<=[,;])\\s+", "");
            requestHeaders.put(headerName, headerValue);
        }

        return requestHeaders;
    }

    public static Map<String, String> getParameterMap(HttpServletRequest servletRequest) {
        Map<String, String> parameterMap = new TreeMap<>();
        Enumeration<String> parameterNames = servletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameterMap.put(name, servletRequest.getParameter(name));
        }

        return parameterMap;
    }

    public static String getUrlPath(HttpServletRequest request) {
        return antPathMatcher.extractPathWithinPattern(
                (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE),
                (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
    }
}
