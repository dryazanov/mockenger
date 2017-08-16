package org.mockenger.standalone;

import org.mockenger.core.config.MockengerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Dmitry Ryazanov
 */
@SpringBootApplication
@Import(MockengerConfig.class)
public class MockengerStandaloneEntryPoint {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MockengerStandaloneEntryPoint.class, args);
    }
}
