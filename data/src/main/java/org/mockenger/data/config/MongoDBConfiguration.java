package org.mockenger.data.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Dmitry Ryazanov
 */
@Configuration
@EnableMongoRepositories(basePackages = {"org.mockenger.data.repository"})
public class MongoDBConfiguration {}
