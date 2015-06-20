package com.socialstartup.mockenger.standalone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by ydolzhenko on 15.06.15.
 */
@SpringBootApplication
public class MockengerStandaloneEntryPoint {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MockengerStandaloneEntryPoint.class, args);
    }

}
