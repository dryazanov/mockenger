package com.socialstartup.mockenger.config;

import com.socialstartup.mockenger.data.config.MockengerDatasourceConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by ydolzhenko on 17.06.15.
 */
@Configuration
@Import(value = MockengerDatasourceConfiguration.class)
public class MockengerCoreConfiguration {
}
