package com.socialstartup.mockenger.core.util;

import com.socialstartup.mockenger.data.model.persistent.mock.request.part.Pair;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public static Set<Pair> getHeaders(HttpServletRequest servletRequest, boolean strictMatch) {
        String headerName;
        String headerValue;
        Set<Pair> requestHeaders = new HashSet<>();
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
            requestHeaders.add(new Pair(headerName, headerValue));
        }

        return requestHeaders;
    }



    public static Header[] getHeadersAsArray(HttpServletRequest servletRequest, boolean strictMatch) {
        String headerName;
        String headerValue;
        List<Header> requestHeaders = new ArrayList<>();
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
            requestHeaders.add(new BasicHeader(headerName, headerValue));
        }

        return (Header[]) requestHeaders.toArray();
    }

    /**
     * Gets all the query parameters and returns them as sorted Map<String, String>
     *
     * @param servletRequest
     * @return
     */
    public static Set<Pair> getParameterMap(HttpServletRequest servletRequest) {
        Set<Pair> parameters = new HashSet<>();
        Enumeration<String> parameterNames = servletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            parameters.add(new Pair(name, servletRequest.getParameter(name)));
        }

        return (parameters.size() > 0 ? parameters : null);
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
