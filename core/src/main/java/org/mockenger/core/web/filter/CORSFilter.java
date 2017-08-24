package org.mockenger.core.web.filter;

import org.mockenger.data.model.dict.RequestMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.mockenger.core.web.controller.base.AbstractController.API_PATH;
import static org.mockenger.data.model.dict.ProjectType.HTTP;
import static org.mockenger.data.model.dict.ProjectType.REST;
import static org.mockenger.data.model.dict.ProjectType.SOAP;
import static org.mockenger.data.model.dict.RequestMethod.OPTIONS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_MAX_AGE;
import static org.springframework.util.StringUtils.arrayToCommaDelimitedString;

/**
 * @author Dmitry Ryazanov
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

    @Value("${mockenger.cors.filter.allow.origin}")
    private String ALLOW_ORIGIN;

    @Value("${mockenger.cors.filter.max.age}")
    private String MAX_AGE;

    @Value("${mockenger.cors.filter.allow.headers}")
    private String ALLOW_HEADERS;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to do
    }


    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {

        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final boolean isMockRequest = isMockRequest(request);

		if (!isMockRequest) {
			response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, ALLOW_ORIGIN);
			response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, arrayToCommaDelimitedString(RequestMethod.values()));
			response.setHeader(ACCESS_CONTROL_MAX_AGE, MAX_AGE);
			response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, ALLOW_HEADERS);
		}

        if (OPTIONS.name().equalsIgnoreCase(request.getMethod()) && !isMockRequest) {
            response.setStatus(SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }


	private boolean isMockRequest(final HttpServletRequest request) {
		return Stream.of(REST, SOAP, HTTP)
				.filter(s -> request.getRequestURI().startsWith(API_PATH + "/" + s + "/"))
				.count() > 0;
	}


	@Override
    public void destroy() {
        // Nothing to do
    }
}
