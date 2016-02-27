package com.socialstartup.mockenger.standalone;

import com.socialstartup.mockenger.core.config.MockengerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by ydolzhenko on 15.06.15.
 */
@SpringBootApplication
@Import(MockengerConfig.class)
public class MockengerStandaloneEntryPoint {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MockengerStandaloneEntryPoint.class, args);
    }
}
