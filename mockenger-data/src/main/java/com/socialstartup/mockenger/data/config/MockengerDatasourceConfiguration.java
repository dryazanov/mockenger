package com.socialstartup.mockenger.data.config;

import com.mongodb.Mongo;
import com.socialstartup.mockenger.data.repository.RequestEntityRepository;
import com.socialstartup.mockenger.data.repository.impl.RequestEntityRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.StringUtils;

/**
 * Created by ydolzhenko on 17.06.15.
 */
@Configuration
@EnableMongoRepositories(basePackages = {"com.socialstartup.mockenger.data.repository"})
public class MockengerDatasourceConfiguration extends AbstractMongoConfiguration {

    @Autowired
    private MockengerDatasourceConfigParams params;

    @Autowired
    private MongoFactoryBean mongoFactoryBean;

    @Override
    protected String getDatabaseName() {
        return params.getDatabase();
    }

    @Bean
    public MongoFactoryBean mongoFactoryBean() {
        MongoFactoryBean mongoFactoryBean = new MongoFactoryBean();
        mongoFactoryBean.setHost(params.getHostname());
        mongoFactoryBean.setPort(params.getPort());
        return mongoFactoryBean;
    }

//    @Override
//    public CustomConversions customConversions() {
//        return new CustomConversions(Arrays.asList());
//    }

    @Bean
    public Mongo mongo() throws Exception {
        return mongoFactoryBean.getObject();
    }

    @Bean
    public GridFsTemplate gridFsTemplate(MongoDbFactory mongoDbFactory, MappingMongoConverter mappingMongoConverter) {
        return new GridFsTemplate(mongoDbFactory, mappingMongoConverter);
    }

    @Override
    public UserCredentials getUserCredentials() {
        if (StringUtils.isEmpty(params.getUsername()) || StringUtils.isEmpty(params.getPassword())) {
            return null;
        } else {
            return new UserCredentials(params.getUsername(), params.getPassword());
        }
    }

    //TODO replace me with spring data's custom
    @Bean
    public RequestEntityRepository requestRepository(MongoTemplate mongoTemplate) {
        return new RequestEntityRepositoryImpl(mongoTemplate);
    }


}
