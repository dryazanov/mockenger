package org.mockenger.dev.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by x079089 on 3/22/2015.
 */
@Configuration
@PropertySource("classpath:mongodb.properties")
public class MongoDbConfig extends AbstractMongoConfiguration {

    @Value("${db.name}")
    private String DB_NAME;

    @Value("${db.host}")
    private String DB_HOST;

    @Value("${db.port}")
    private int DB_PORT;

    @Override
    protected String getDatabaseName() {
        return DB_NAME;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(DB_HOST, DB_PORT);
    }
}
