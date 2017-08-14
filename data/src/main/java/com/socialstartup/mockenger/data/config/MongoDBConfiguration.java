package com.socialstartup.mockenger.data.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Dmitry Ryazanov
 */
@Configuration
@EnableMongoRepositories(basePackages = {"com.socialstartup.mockenger.data.repository"})
public class MongoDBConfiguration {}
