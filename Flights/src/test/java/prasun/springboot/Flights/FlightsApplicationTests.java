package prasun.springboot.Flights;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import prasun.springboot.flights.entity.AirlineInfo;
import prasun.springboot.flights.entity.Flight;
import prasun.springboot.flights.service.AirlineInfoService;
import prasun.springboot.flights.service.FlightService;

@SpringBootTest
@Transactional
class FlightsApplicationTests {

	@Autowired
	private AirlineInfoService airlineService;
	
    @Autowired
    private FlightService flightService;


	@Test
	void contextLoads() {
		AirlineInfo airlineInfo = new AirlineInfo("vistara.png", "Vistara");
		airlineService.save(airlineInfo);
	}

	@Test
	void saveFlight() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
		Date date = sdf.parse("22/08/2020");
        Date time = stf.parse("03:15");
        Flight flt = flightService.saveFlight("Air India", 100, "INR", 2000.00,
                "AI-100", "Boeing", "Pune", "Chennai", date, time, "2 hrs 00 mins");
        assertNotNull(flt);
	}

}
