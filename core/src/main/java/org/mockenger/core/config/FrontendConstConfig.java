package org.mockenger.core.config;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Dmitry Ryazanov
 */
public class FrontendConstConfig {

	@Value("${frontend.constants.environment}")
	private String environment;

	@Value("${frontend.constants.api.base.url}")
	private String apiBaseUrl;

	@Value("${oauth2.client.app.name}")
	private String clientAppName;

	@Value("${oauth2.client.app.secret}")
	private String clientAppSecret;

	@Value("${app.version}")
	private String appVersion;

	@Value("${build.timestamp}")
	private String buildTimestamp;

	@Value("${frontend.audit.log.events.per.page}")
	public int itemsPerPage = 25;


	@Bean(name = "constantsBean")
	public String getConstants(final ApplicationContext applicationContext) {
		final Resource resource = applicationContext.getResource("classpath:static/js/constants.js");

		InputStream is = null;

		try {
			is = resource.getInputStream();

			return setConfigValues(applicationContext, IOUtils.toString(is, UTF_8));
		} catch (IOException e) {
			throw new Error("Failed to load constants from the resource file");
		} finally {
			if (Objects.nonNull(is)) {
				IOUtils.closeQuietly(is);
			}
		}
	}


	/**
	 * ENV
	 * SECURITY
	 * SECRET_KEY
	 * API_BASE_PATH
	 * APP_VERSION
	 * REQUESTS_PER_PAGE
	 * BUILD_DATE
	 *
	 * @param applicationContext
	 * @param constantsTemplate
	 * @return
	 */
	private String setConfigValues(final ApplicationContext applicationContext, final String constantsTemplate) {
		final boolean isSecurityOn = applicationContext.getEnvironment().acceptsProfiles("security");
		final String secretKey = Base64Utils.encodeToString((clientAppName + ":" + clientAppSecret).getBytes());

		return format(constantsTemplate, environment, isSecurityOn, secretKey, apiBaseUrl, appVersion, itemsPerPage, buildTimestamp);
	}
}
