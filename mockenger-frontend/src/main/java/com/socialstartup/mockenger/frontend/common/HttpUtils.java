package com.socialstartup.mockenger.frontend.common;

import com.socialstartup.mockenger.data.model.RequestType;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by x079089 on 3/20/2015.
 */
public class HttpUtils {

    private static HashMap<RequestMethod, RequestType> requestMethodsToTypesMap = new HashMap<>();
    private static HashMap<RequestType, RequestMethod> requestTypesToMethodsMap = new HashMap<>();

    static {
        requestMethodsToTypesMap.put(RequestMethod.DELETE, RequestType.DELETE);
        requestMethodsToTypesMap.put(RequestMethod.GET, RequestType.GET);
        requestMethodsToTypesMap.put(RequestMethod.HEAD, RequestType.HEAD);
        requestMethodsToTypesMap.put(RequestMethod.OPTIONS, RequestType.OPTIONS);
        requestMethodsToTypesMap.put(RequestMethod.PATCH, RequestType.PATCH);
        requestMethodsToTypesMap.put(RequestMethod.POST, RequestType.POST);
        requestMethodsToTypesMap.put(RequestMethod.PUT, RequestType.PUT);
        requestMethodsToTypesMap.put(RequestMethod.TRACE, RequestType.TRACE);
        requestMethodsToTypesMap.entrySet()
                .stream().forEach(entry -> requestTypesToMethodsMap.put(entry.getValue(), entry.getKey()));
    }

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

    public static RequestType getRequestTypeByMethod(RequestMethod requestMethod) {
        if (!requestMethodsToTypesMap.containsKey(requestMethod)) {
            throw new IllegalArgumentException(String.format("Request method '%s' is unknown to mapper", requestMethod));
        }
        return requestMethodsToTypesMap.get(requestMethod);
    }

    public static RequestMethod getRequestMethodByType(RequestType requestType) {
        if (!requestTypesToMethodsMap.containsKey(requestType)) {
            throw new IllegalArgumentException(String.format("Request type '%s' is unknown to mapper", requestType));
        }
        return requestTypesToMethodsMap.get(requestType);
    }
}
