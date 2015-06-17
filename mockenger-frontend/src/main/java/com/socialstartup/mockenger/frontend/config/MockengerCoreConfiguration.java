package com.socialstartup.mockenger.frontend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ydolzhenko on 17.06.15.
 */
@Configuration
@ComponentScan(basePackages = {
        "com.socialstartup.mockenger.data.config",
        "com.socialstartup.mockenger.frontend.service",
        "com.socialstartup.mockenger.frontend.controller"
})
public class MockengerCoreConfiguration {
}
