package prasun.springboot.flights;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlightsApplication {

	public static void main(String[] args) {
		// Enable MongoDB logging in general
		System.setProperty("DEBUG.MONGO", "true");

		// Enable DB operation tracing
		System.setProperty("DB.TRACE", "true");

		SpringApplication.run(FlightsApplication.class, args);
	}

}
