package com.socialstartup.mockenger.standalone.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ydolzhenko on 15.06.15.
 */
@Configuration()
@Controller
@EnableAutoConfiguration
public class MockengerStandaloneEntryPoint {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MockengerStandaloneEntryPoint.class, args);
    }


}
