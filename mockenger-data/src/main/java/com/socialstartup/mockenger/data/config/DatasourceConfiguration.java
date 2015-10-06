package com.socialstartup.mockenger.data.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClientOptions;
import com.mongodb.WriteConcern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.StringUtils;

/**
 * Created by ydolzhenko on 17.06.15.
 */
@Configuration
@EnableMongoRepositories(basePackages = {"com.socialstartup.mockenger.data.repository"})
public class DatasourceConfiguration extends AbstractMongoConfiguration {

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

    @Autowired
    private MongoClientFactoryBean mongoClientFactoryBean;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    public MongoClientFactoryBean mongoFactoryBean() {
        MongoClientFactoryBean mongoFactoryBean = new MongoClientFactoryBean();
        mongoFactoryBean.setHost(hostname);
        mongoFactoryBean.setPort(port);
        mongoFactoryBean.setMongoClientOptions(MongoClientOptions.builder().writeConcern(WriteConcern.SAFE).build());
        return mongoFactoryBean;
    }

    @Bean
    public Mongo mongo() throws Exception {
        return mongoClientFactoryBean.getObject();
    }

    @Override
    public UserCredentials getUserCredentials() {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        } else {
            return new UserCredentials(username, password);
        }
    }

}
