package com.socialstartup.mockenger.core.config;

import com.socialstartup.mockenger.data.config.MockengerDatasourceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by ydolzhenko on 17.06.15.
 */
@Configuration
@Import(value = MockengerDatasourceConfiguration.class)
@ComponentScan(
        basePackages = {"com.socialstartup.mockenger.core.service"},
        basePackageClasses = {MockengerDatasourceConfiguration.class, MockengerHeadersStopListConfigParam.class}
)
public class MockengerCoreConfiguration {}