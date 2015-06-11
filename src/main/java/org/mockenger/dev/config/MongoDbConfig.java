package org.mockenger.dev.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by x079089 on 3/22/2015.
 */
@Configuration
public class MongoDbConfig extends AbstractMongoConfiguration {

    private final String DB_NAME = "mock_server";

    private final String DB_HOST = "localhost";

    private final int DB_PORT = 27017;

    @Override
    protected String getDatabaseName() {
        return DB_NAME;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(DB_HOST, DB_PORT);
    }
}
