package org.mockenger.core.util;

import com.google.common.collect.ImmutableList;
import org.apache.http.client.utils.URLEncodedUtils;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.Collections.list;
import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.ACCEPT_CHARSET;
import static org.springframework.http.HttpHeaders.ACCEPT_ENCODING;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.HttpHeaders.ACCEPT_RANGES;
import static org.springframework.http.HttpHeaders.AGE;
import static org.springframework.http.HttpHeaders.ALLOW;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONNECTION;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_ENCODING;
import static org.springframework.http.HttpHeaders.CONTENT_LANGUAGE;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_LOCATION;
import static org.springframework.http.HttpHeaders.CONTENT_RANGE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpHeaders.COOKIE;
import static org.springframework.http.HttpHeaders.DATE;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpHeaders.EXPECT;
import static org.springframework.http.HttpHeaders.EXPIRES;
import static org.springframework.http.HttpHeaders.FROM;
import static org.springframework.http.HttpHeaders.HOST;
import static org.springframework.http.HttpHeaders.IF_MATCH;
import static org.springframework.http.HttpHeaders.IF_MODIFIED_SINCE;
import static org.springframework.http.HttpHeaders.IF_NONE_MATCH;
import static org.springframework.http.HttpHeaders.IF_RANGE;
import static org.springframework.http.HttpHeaders.IF_UNMODIFIED_SINCE;
import static org.springframework.http.HttpHeaders.LAST_MODIFIED;
import static org.springframework.http.HttpHeaders.LINK;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpHeaders.MAX_FORWARDS;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpHeaders.PRAGMA;
import static org.springframework.http.HttpHeaders.PROXY_AUTHENTICATE;
import static org.springframework.http.HttpHeaders.PROXY_AUTHORIZATION;
import static org.springframework.http.HttpHeaders.RANGE;
import static org.springframework.http.HttpHeaders.REFERER;
import static org.springframework.http.HttpHeaders.RETRY_AFTER;
import static org.springframework.http.HttpHeaders.SERVER;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpHeaders.SET_COOKIE2;
import static org.springframework.http.HttpHeaders.TE;
import static org.springframework.http.HttpHeaders.TRAILER;
import static org.springframework.http.HttpHeaders.TRANSFER_ENCODING;
import static org.springframework.http.HttpHeaders.UPGRADE;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.HttpHeaders.VARY;
import static org.springframework.http.HttpHeaders.VIA;
import static org.springframework.http.HttpHeaders.WARNING;
import static org.springframework.http.HttpHeaders.WWW_AUTHENTICATE;
import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;
import static org.springframework.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE;

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
     * Gets all the headers from request and returns them as Set<String>
     *
     * @param servletRequest
     * @param strictMatch true - if you want to use headers as they are, false - will set everything to lower case
     * @return
     */
    public static Set<Pair> getHeaders(final HttpServletRequest servletRequest, boolean strictMatch) {
		return list(servletRequest.getHeaderNames())
				.stream()
				.map(name -> {
					String value = servletRequest.getHeader(name);

					if (!strictMatch) {
						name = name.toLowerCase();
						value = value.toLowerCase();
					}

					value = value.replaceAll(DELIMITER_PATTERN, "");

					return new Pair(name, value);
				})
				.collect(toSet());
    }


    /**
     * Gets all the query parameters and returns them as Set<Pair>
     *
     * @param servletRequest
     * @return
     */
    public static Set<Pair> getParameterSet(final HttpServletRequest servletRequest) {
		return list(servletRequest.getParameterNames())
				.stream()
				.map(name -> new Pair(name, servletRequest.getParameter(name)))
				.collect(toSet());
    }


	/**
	 * Gets all the query parameters and returns them as SortedSet<Pair>
	 *
	 * @param servletRequest
	 * @return
	 */
	public static SortedSet<Pair> getParameterSortedSet(final HttpServletRequest servletRequest) {
		return new TreeSet(getParameterSet(servletRequest));
	}


	/**
	 * Parses URL query string and returns Set<Pair>
	 *
	 * @param stringToParse URL query string - <i>param1=value1&param2=value</i>
	 * @return
	 */
	public static Set<Pair> getParameterSet(final String stringToParse) {
		return URLEncodedUtils.parse(stringToParse, UTF_8)
				.stream()
				.map(p -> new Pair(p.getName(), p.getValue()))
				.collect(toSet());
	}


	/**
	 * Parses URL query string and returns SortedSet<Pair>
	 *
	 * @param stringToParse URL query string - <i>param1=value1&param2=value</i>
	 * @return
	 */
	public static SortedSet<Pair> getParameterSortedSet(final String stringToParse) {
		return new TreeSet(getParameterSet(stringToParse));
	}


    /**
     * Gets request path
     *
     * @param servletRequest
     * @return
     */
    public static String getUrlPath(final HttpServletRequest servletRequest) {
		final String servletPathAttribute = (String) servletRequest.getAttribute(PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		final String pathPatternAttribute = (String) servletRequest.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);

		return antPathMatcher.extractPathWithinPattern(pathPatternAttribute, servletPathAttribute) +
				(servletPathAttribute.endsWith("/") ? "/" : "");
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
