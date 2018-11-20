package web.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;

import web.globalvars.DbConfig;

@EnableMongoRepositories("web")
@Configuration
@ComponentScan("web")
public class MongoConfig {
	
	@Autowired
	private DbConfig config;
	
	@Bean
	public MongoClient mongoClient() {
		return new MongoClient(config.getHOST(), config.getPORT());
	}
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {		
		return new MongoTemplate(mongoClient(), config.getDATABASE());
	}
	
}
