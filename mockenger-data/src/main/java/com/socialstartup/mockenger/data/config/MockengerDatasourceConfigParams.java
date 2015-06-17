package com.socialstartup.mockenger.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ydolzhenko on 17.06.15.
 */
@Component
public class MockengerDatasourceConfigParams {

    @Value("${mockenger.datasource.database}")
    private String database;

    @Value("${mockenger.datasource.hostname}")
    private String hostname;

    @Value("${mockenger.datasource.port}")
    private int port;

    @Value("${mockenger.datasource.username:}")
    private String username;

    @Value("${mockenger.datasource.password:}")
    private String password;

    public String getDatabase() {
        return database;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
