package co.com.btgpactual.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "co.com.btgpactual.repository")
public class MongoConfig {

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoMappingContext context) {
        var converter = new MappingMongoConverter(ReactiveMongoTemplate.NO_OP_REF_RESOLVER, context);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }
}
