package com.socialstartup.mockenger.standalone.config;

import com.socialstartup.mockenger.config.MockengerCoreConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Created by ydolzhenko on 15.06.15.
 */
@Import(value = MockengerCoreConfiguration.class)
public class MockengerStandaloneConfig {
}
