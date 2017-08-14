package org.mockenger.core.util;

import com.google.common.collect.ImmutableList;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
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

import static org.springframework.http.HttpHeaders.*;

/**
 * Created by Dmitry Ryazanov on 3/20/2015.
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

        return requestHeaders.toArray(new Header[requestHeaders.size()]);
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

    /**
     *
     * @return list of headers (excluding ACCESS_CONTROL_*)
     */
    public static List<String> getListOfHeaders() {
        return ImmutableList.of(
                ACCEPT,
                ACCEPT_CHARSET,
                ACCEPT_ENCODING,
                ACCEPT_LANGUAGE,
                ACCEPT_RANGES,
                AGE,
                ALLOW,
                AUTHORIZATION,
                CACHE_CONTROL,
                CONNECTION,
                CONTENT_ENCODING,
                CONTENT_DISPOSITION,
                CONTENT_LANGUAGE,
                CONTENT_LENGTH,
                CONTENT_LOCATION,
                CONTENT_RANGE,
                CONTENT_TYPE,
                COOKIE,
                DATE,
                ETAG,
                EXPECT,
                EXPIRES,
                FROM,
                HOST,
                IF_MATCH,
                IF_MODIFIED_SINCE,
                IF_NONE_MATCH,
                IF_RANGE,
                IF_UNMODIFIED_SINCE,
                LAST_MODIFIED,
                LINK,
                LOCATION,
                MAX_FORWARDS,
                ORIGIN,
                PRAGMA,
                PROXY_AUTHENTICATE,
                PROXY_AUTHORIZATION,
                RANGE,
                REFERER,
                RETRY_AFTER,
                SERVER,
                SET_COOKIE,
                SET_COOKIE2,
                TE,
                TRAILER,
                TRANSFER_ENCODING,
                UPGRADE,
                USER_AGENT,
                VARY,
                VIA,
                WARNING,
                WWW_AUTHENTICATE);
    }
}
