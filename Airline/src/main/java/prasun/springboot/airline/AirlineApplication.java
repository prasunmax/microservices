package prasun.springboot.airline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AirlineApplication {

	public static void main(String[] args) {
		// Enable MongoDB logging in general
		System.setProperty("DEBUG.MONGO", "true");

		// Enable DB operation tracing
		System.setProperty("DB.TRACE", "true");

		SpringApplication.run(AirlineApplication.class, args);
	}

}
