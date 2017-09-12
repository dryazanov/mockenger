package org.mockenger.core.util;

import lombok.extern.slf4j.Slf4j;
import org.mockenger.data.model.persistent.mock.request.AbstractRequest;
import org.mockenger.data.model.persistent.mock.request.GenericRequest;
import org.mockenger.data.model.persistent.mock.request.part.Body;
import org.mockenger.data.model.persistent.mock.request.part.Headers;
import org.mockenger.data.model.persistent.mock.request.part.Pair;
import org.mockenger.data.model.persistent.mock.request.part.Parameters;
import org.mockenger.data.model.persistent.mock.request.part.Path;

import java.util.Date;
import java.util.Set;

import static java.util.Collections.EMPTY_SET;
import static java.util.Optional.ofNullable;
import static org.mockenger.core.util.CommonUtils.getCheckSum;
import static org.mockenger.core.util.CommonUtils.startAndEndsWith;
import static org.mockenger.data.model.dict.RequestMethod.PATCH;
import static org.mockenger.data.model.dict.RequestMethod.POST;
import static org.mockenger.data.model.dict.RequestMethod.PUT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * @author Dmitry Ryazanov
 */
@Slf4j
public class MockRequestUtils {

	public static final String APPLICATION_XML_PATTERN = "^[application|text]+\\/.*\\+?xml.*";
	public static final String APPLICATION_JSON_PATTERN = "^[application]+\\/.*\\+?json.*";


	public static boolean isMultipartFormData(final Headers headers) {
		return containsHeader(headers, CONTENT_TYPE, MULTIPART_FORM_DATA_VALUE);
	}


	public static boolean isURLEncodedForm(final Headers headers) {
		return containsHeader(headers, CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
	}


	public static boolean containsHeader(final Headers headers, final String headerName, final String headerValue) {
		return getHeaderValues(headers)
				.stream()
				.filter(p -> headerName.equalsIgnoreCase(p.getKey()) && p.getValue().contains(headerValue))
				.count() > 0;
	}


	public static boolean hasContentType(final Headers headers, final String pattern) {
		return getHeaderValues(headers)
				.stream()
				.filter(p -> CONTENT_TYPE.equalsIgnoreCase(p.getKey())
						&& p.getValue().toLowerCase().matches(pattern))
				.count() > 0;
	}


	public static Headers getHeaders(final GenericRequest genericRequest) {
		return ofNullable(genericRequest)
				.map(GenericRequest::getHeaders)
				.orElse(new Headers());
	}


	public static Set<Pair> getHeaderValues(final GenericRequest genericRequest) {
		return ofNullable(genericRequest)
				.map(h -> getHeaderValues(h.getHeaders()))
				.orElse(EMPTY_SET);
	}


	public static Set<Pair> getHeaderValues(final Headers headers) {
		return ofNullable(headers)
				.map(Headers::getValues)
				.orElse(EMPTY_SET);
	}


	public static Parameters getParameters(final GenericRequest genericRequest) {
		return ofNullable(genericRequest)
				.map(GenericRequest::getParameters)
				.orElse(new Parameters());
	}


	public static Set<Pair> getParamValues(final GenericRequest genericRequest) {
		return ofNullable(genericRequest)
				.map(p -> getParamValues(p.getParameters()))
				.orElse(EMPTY_SET);
	}


	public static Set<Pair> getParamValues(final Parameters parameters) {
		return ofNullable(parameters)
				.map(Parameters::getValues)
				.orElse(EMPTY_SET);
	}


	public static Path getPath(final GenericRequest genericRequest) {
		return ofNullable(genericRequest)
				.map(GenericRequest::getPath)
				.orElse(new Path());
	}


	public static String getPathValue(final GenericRequest genericRequest) {
		return ofNullable(genericRequest)
				.map(p -> getPathValue(p.getPath()))
				.orElse("");
	}


	public static String getPathValue(final Path path) {
		return ofNullable(path)
				.map(Path::getValue)
				.orElse("");
	}


	public static Body getBody(final GenericRequest genericRequest) {
		return ofNullable(genericRequest)
				.map(GenericRequest::getBody)
				.orElse(new Body());
	}


	public static String getBodyValue(final GenericRequest genericRequest) {
		return ofNullable(genericRequest)
				.map(p -> getBodyValue(p.getBody()))
				.orElse("");
	}


	public static String getBodyValue(final Body body) {
		return ofNullable(body)
				.map(Body::getValue)
				.orElse("");
	}


	public static boolean isHttpMethodWithBody(final GenericRequest request) {
		return ofNullable(request)
				.map(GenericRequest::getMethod)
				.map(m -> m.equals(POST) || m.equals(PUT) || m.equals(PATCH))
				.orElse(false);
	}


	public static boolean isXml(final Headers headers, final String body) {
		return isXml(headers) || startAndEndsWith(body.trim(), "<", ">");
	}


	public static boolean isXml(final Headers headers) {
		return hasContentType(headers, APPLICATION_XML_PATTERN);
	}


	public static boolean isJson(final Headers headers, final String body) {
		return isJson(headers) || startAndEndsWith(body.trim(), "{", "}");
	}


	public static boolean isJson(final Headers headers) {
		return hasContentType(headers, APPLICATION_JSON_PATTERN);
	}


	public static AbstractRequest toAbstractRequest(final GenericRequest genericRequest) {
		final Date now = new Date();
		final AbstractRequest abstractRequest = new AbstractRequest();

		abstractRequest.setGroupId(genericRequest.getGroupId());
		abstractRequest.setMethod(genericRequest.getMethod());
		abstractRequest.setName(String.valueOf(now.getTime()));
		abstractRequest.setCreationDate(now);
		abstractRequest.setPath(genericRequest.getPath());
		abstractRequest.setParameters(genericRequest.getParameters());
		abstractRequest.setHeaders(genericRequest.getHeaders());
		abstractRequest.setBody(genericRequest.getBody());
		abstractRequest.setCheckSum(getCheckSum(genericRequest));

		return abstractRequest;
	}
}
