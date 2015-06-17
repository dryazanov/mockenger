package com.socialstartup.mockenger.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ydolzhenko on 15.06.15.
 */
@SpringBootApplication
public class MockengerStandaloneEntryPoint {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MockengerStandaloneEntryPoint.class, args);
    }

}
