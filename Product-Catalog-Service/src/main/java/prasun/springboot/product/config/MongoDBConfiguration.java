package prasun.springboot.product.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@Service
public class MongoDBConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MongoClient mongoClient(@Value("${spring.mongodb.uri}") String connectionString) {

        ConnectionString connString = new ConnectionString(connectionString);

        // Ticket: Handling Timeouts - configure the expected
        // WriteConcern `wtimeout` and `connectTimeoutMS` values
        MongoClientSettings settings = MongoClientSettings.builder()
        		.applyConnectionString(connString)
        		.applicationName("product-catalog-service")
        		.applyToClusterSettings(block -> block
        				.applyConnectionString(connString)
        				.serverSelectionTimeout(50000, TimeUnit.MILLISECONDS)
        				.build()
        		)
        		.applyToConnectionPoolSettings(block -> 
					block
					.applyConnectionString(connString)
	        		.maxSize(50)
	        		.build()
				 )
        		.writeConcern(new WriteConcern("majority").withWTimeout(2500, TimeUnit.MILLISECONDS))
        		.applyToSocketSettings(block -> block
        				.applyConnectionString(connString)
        				.connectTimeout(2000, TimeUnit.MILLISECONDS)//For the test to pass it is also mentioned in the connection string
        				.build()
        		 )        		
        		.build();
        MongoClient mongoClient = MongoClients.create(settings);
        

        return mongoClient;
    }
}
