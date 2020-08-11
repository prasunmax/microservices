package prasun.springboot.Flights;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import prasun.springboot.flights.FlightsApplication;
import prasun.springboot.flights.entity.Flight;
import prasun.springboot.flights.service.FlightService;

@SpringBootTest(classes = FlightsApplication.class)
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
class FlightsApplicationTests {
	//private static final Logger log = LoggerFactory.getLogger(FlightsApplicationTests.class);


	
    @Autowired
    private FlightService flightService;
	
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat stf = new SimpleDateFormat("HH:mm");

    
    @BeforeAll
    void setup() {
    }
    
    @Test
	void saveandSearchAirline() throws ParseException {
    	//Save Airline 
//	    airlineInfo = airlineService.findByAirlineName("DUMMY AIRLINE");
//	    assertNotNull(airlineInfo);    	
    }

	@Test
	void saveandSearchFlight() throws ParseException {
		Date date = sdf.parse("22/08/2020");
        Date time = stf.parse("03:15");
    	//Save Airline 
	    	
        Flight flt = flightService.saveFlight("Dummy Airline", 100, "INR", 2000.00,
                "AI-100", "Boeing", "Pune", "Chennai", date, time, "2 hrs 00 mins");
        assertNotNull(flt);
        flt = flightService.findByFltDateAndFltNo(date, "AI-100");
        assertNotNull(flt);
	}

}
