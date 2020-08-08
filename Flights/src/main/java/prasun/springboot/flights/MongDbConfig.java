package prasun.springboot.flights;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.ogm.jpa.HibernateOgmPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })

@EnableJpaRepositories(basePackages = {
		"prasun.springboot.flights" }, entityManagerFactoryRef = "mongoEntityManager", transactionManagerRef = "mongoTransactionManager")
@EnableTransactionManagement
public class MongDbConfig extends AbstractMongoClientConfiguration{

	@Value("${hibernate.ogm.datastore.provider}")
	private String provider;

	@Value("${hibernate.ogm.datastore.host}")
	private String host;

	@Value("${hibernate.ogm.datastore.port}")
	private String port;

	@Value("${hibernate.ogm.datastore.database}")
	private String database;

	@Value("${hibernate.ogm.datastore.create_database}")
	private String crtDB;

	@Value("${hibernate.ogm.datastore.username}")
	private String username;

	@Value("${hibernate.ogm.datastore.password}")
	private String password;

	@Bean(name = "mongoEntityManager")
	public LocalContainerEntityManagerFactoryBean mongoEntityManager() throws Throwable {

		Map<String, Object> properties = new HashMap<String, Object>();
		//properties.put("javax.persistence.transactionType", "JTA");
		properties.put("javax.persistence.transactionType", "resource_local");
		properties.put("hibernate.ogm.datastore.provider", provider);
		properties.put("hibernate.ogm.datastore.host", host);
		properties.put("hibernate.ogm.datastore.port", port);
		properties.put("hibernate.ogm.datastore.database", database);
		properties.put("hibernate.ogm.datastore.create_database", crtDB);
		properties.put("hibernate.ogm.datastore.username", username);
		properties.put("hibernate.ogm.datastore.password", password);
		properties.put("hibernate.ogm.mongodb.authentication_database", "admin");
		properties.put("hibernate.ogm.mongodb.connection_timeout", "5000");
		properties.put("hibernate.ogm.datastore.document.association_storage", "IN_ENTITY");
		properties.put("hibernate.ogm.mongodb.association_document_storage", "GLOBAL_COLLECTION");
		properties.put("hibernate.ogm.mongodb.write_concern_type", "JOURNALED");
		properties.put("hibernate.ogm.mongodb.write_concern", "MAJORITY");
		properties.put("hibernate.ogm.mongodb.read_preference", "PRIMARY_PREFERRED");

		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();

		LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
		entityManager.setJpaVendorAdapter(vendorAdapter);
		entityManager.setPackagesToScan("prasun.springboot.flights.entity");// This is required so that the hibernate
																			// doesnot go looking for the _persistence
																			// xml.
		entityManager.setPersistenceUnitName("mongoPersistenceUnit");
		//entityManager.set
		entityManager.setJpaPropertyMap(properties);
		entityManager.setPersistenceProviderClass(HibernateOgmPersistence.class);
		return entityManager;
	}

	@Bean(name = "mongoTransactionManager")
	public PlatformTransactionManager transactionManager() throws Throwable {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(mongoEntityManager().getNativeEntityManagerFactory());
		//transactionManager.setEntityManagerFactory(mongoEntityManager().getObject());
		return transactionManager;
	}

	@Override
	protected String getDatabaseName() {
		return database;
	}
	
	/*
	 * @Override public MongoClient mongoClient() {
	 * 
	 * return (MongoClient) new com.mongodb.MongoClient(host,
	 * Integer.parseInt(port)); }
	 */

}