package com.socialstartup.mockenger.frontend.common;

import org.springframework.util.*;
import org.springframework.web.servlet.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by x079089 on 3/20/2015.
 */
public class HttpUtils {

    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static Map<String, String> getHeaders(HttpServletRequest servletRequest, boolean strictMatch) {
        Map<String, String> requestHeaders = new HashMap<String, String>();
        Enumeration<String> headerNames = servletRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            if (strictMatch) {
                requestHeaders.put(name, servletRequest.getHeader(name));
            } else {
                requestHeaders.put(name.toLowerCase(), servletRequest.getHeader(name).toLowerCase());
            }
        }

        return requestHeaders;
    }

    public static Map<String, String> getParameterMap(HttpServletRequest servletRequest) {
        Map<String, String> parameterMap = new TreeMap<String, String>();
        Enumeration<String> parameterNames = servletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameterMap.put(name, servletRequest.getParameter(name));
        }

//        System.out.println(parameterMap);

        return parameterMap;
    }

    public static String getUrlPath(HttpServletRequest request) {
        return antPathMatcher.extractPathWithinPattern(
                (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE),
                (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE));
    }
}
