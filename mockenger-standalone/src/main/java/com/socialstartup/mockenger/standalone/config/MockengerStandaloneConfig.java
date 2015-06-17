package com.socialstartup.mockenger.standalone.config;

import com.socialstartup.mockenger.core.config.MockengerCoreConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by ydolzhenko on 15.06.15.
 */
@Configuration
@Import(value = MockengerCoreConfiguration.class)
@ComponentScan(basePackages = {
        "com.socialstartup.mockenger.core.web"
})
public class MockengerStandaloneConfig {
}
