package prasun.springboot.flights.commandline;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import prasun.springboot.flights.entity.AirlineInfo;
import prasun.springboot.flights.entity.Flight;
import prasun.springboot.flights.service.AirlineInfoService;
import prasun.springboot.flights.service.FlightService;

@Component
public class UserCommandLineRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory
            .getLogger(UserCommandLineRunner.class);

    @Autowired
    private AirlineInfoService airlineService;

    @Autowired
    private FlightService flightService;

    @Bean 
    public void test() {
    	System.out.println("test");
    }

    @Override
    public void run(String... args) throws ParseException, NotSupportedException, SystemException {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = sdf.parse("21/08/2020");
//        SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
////        String indigoFltStr = "indigo";
////        List<Flight> flts = flightService.findByFltDateAndAirline(date, indigoFltStr);
////        flts.stream().forEach(flight -> log.info(flight.toString()));
//        date = sdf.parse("22/08/2020");
//        Date time = stf.parse("03:15");
////        Flight flt = flightService.saveFlight("Air India", 100, "INR", 2000.00,
////                "AI-100", "Boeing", "Pune", "Chennai", date, time, "2 hrs 00 mins");
    	
    }
    public void assignmentRun() throws ParseException {

        log.info("-------------------------------");
        log.info("0.1: Finding all Airlines, this is for testing only");
        log.info("-------------------------------");
        for (AirlineInfo info : airlineService.findAll()) {
            log.info(info.toString());
        }

        log.info("-------------------------------");
        log.info("0.2: Retrieve all Indigo flight trips of August month, was present in the first assignment");
        log.info("-------------------------------");


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = sdf.parse("21/08/2020");
        String indigoFltStr = "6E%";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
       
        log.info("-------------------------------");
        log.info("1. Retrieve all Indigo flight trips on August 21st 2020, based on flights starting with 6E");
        log.info("-------------------------------");
        List<Flight> flts = flightService.findByFltDateAndFltNoLike(date, indigoFltStr);
        for (Flight flt : flts) {
            log.info(flt.toString());
        }
        log.info("-------------------------------");
        log.info("1. Retrieve all Indigo flight trips on August 21st 2020, based on joining Airline_info table");
        log.info("-------------------------------");
        indigoFltStr = "indigo";
        flts = flightService.findByFltDateAndAirline(date, indigoFltStr);
        for (Flight flt : flts) {
            log.info(flt.toString());
        }

        log.info("-------------------------------");
        log.info("2. Find all the flights leaving from Delhi airport on 21st of August 2020");
        log.info("-------------------------------");
        flts = flightService.findByFltDateAndOrigin(date, "delhi");
        for (Flight flt : flts) {
            log.info(flt.toString());
        }
        log.info("-------------------------------");
        log.info("3. Search for flights frying between Delhi to Chennai on 21st August 2020");
        log.info("-------------------------------");
        flts = flightService.findByFltDateAndOriginAndDestination(date, "delhi", "chennai");
        for (Flight flt : flts) {
            log.info(flt.toString());
        }

        log.info("-------------------------------");
        log.info("4. Search for flights with combination of flight number origin and destination.");
        log.info("-------------------------------");

        flts = flightService.findByOriginAndDestinationAndFltNo("delhi", "chennai", "ai-840");
        for (Flight flt : flts) {
            log.info(flt.toString());
        }


        log.info("-------------------------------");
        log.info("5. Search for flight with combination of flight number, flight date and flight time.");
        log.info("-------------------------------");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
        Date time = stf.parse("03:15"); //Anomaly between database and code will need to fix this
        Flight flt = flightService.findByFltNoAndFltDateAndTime("6E-6685", date, time);
        if (null != flt) {
            log.info(flt.toString());
        }

        log.info("-------------------------------");
        log.info("6. Schedule 6 additional flights from Pune to Chennai on August 22nd with different times on the same day. " +
                "Schedule 3 flights of AirIndia. Make sure flight number must not repeat on the sameday for same origin and destination. " +
                "Also make sure that same flight number must not be scheduled on the sameday from any origin and destination.");
        log.info("-------------------------------");
        date = sdf.parse("22/08/2020");
        time = stf.parse("03:15");
        flt = flightService.saveFlight("Air India", 100, "INR", 2000.00,
                "AI-100", "Boeing", "Pune", "Chennai", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }
        time = stf.parse("05:15");
        flt = flightService.saveFlight("Air India", 100, "INR", 2000.00,
                "AI-101", "Boeing", "Pune", "Chennai", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }
        time = stf.parse("07:15");
        flt = flightService.saveFlight("Air India", 100, "INR", 2000.00,
                "AI-102", "Boeing", "Pune", "Chennai", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }
        time = stf.parse("09:15");
        flt = flightService.saveFlight("Air India", 100, "INR", 2000.00,
                "AI-103", "Boeing", "Pune", "Chennai", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }
        time = stf.parse("11:15");
        flt = flightService.saveFlight("Air India", 100, "INR", 2000.00,
                "AI-104", "Boeing", "Pune", "Chennai", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }
        time = stf.parse("13:15");
        flt = flightService.saveFlight("Air India", 100, "INR", 2000.00,
                "AI-105", "Boeing", "Pune", "Chennai", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }
        log.info("-------------------------------");
        log.info("7. Book a flight for 2 passengers from Delhi to Hyderabad on 21st August for 01:15AM," +
                "and update inventory accordingly ");
        log.info("-------------------------------");

        log.info("get the flight information");
        time = stf.parse("01:15");
        date = sdf.parse("21/08/2020");
        flts = flightService.findByFltDateAndTime(date, time);

        log.info("-------------------------------");
        log.info("8. Write a class to add 4 more flights from Delhi to Pune on 21st August for your desired timings." +
                "Make sure you are adding 1 Indigo,1 Air Asia,1 Vistara and 1 Air India flight. ");
        log.info("-------------------------------");

        date = sdf.parse("21/08/2020");
        time = stf.parse("09:15");
        flt = flightService.saveFlight("Indigo", 100, "INR", 5000.00,
                "6E-6690", "Boeing", "Delhi", "Pune", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }
        time = stf.parse("10:15");
        flt = flightService.saveFlight("Air India", 100, "INR", 5000.00,
                "AI-120", "Boeing", "Delhi", "Pune", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }
        time = stf.parse("11:15");
        flt = flightService.saveFlight("Vistara", 100, "INR", 5000.00,
                "UK-990", "Boeing", "Delhi", "Pune", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }
        time = stf.parse("10:15");
        flt = flightService.saveFlight("Air Asia", 100, "INR", 5000.00,
                "I5-800", "Boeing", "Delhi", "Pune", date, time, "2 hrs 00 mins");
        if (null != flt) {
            log.info("\nFlight added:" + flt.toString());
        }

        log.info("-------------------------------");
        log.info("9. Write a class to Update Indigo flight which is scheduled on 21st August 2020 3:15 AM" +
                "from Delhi to Pune,and reschedule it to 22nd August 2020 6:30PM for the flight number 6E-6686");
        log.info("-------------------------------");
        date = sdf.parse("21/08/2020");
        time = stf.parse("03:15");
        flt = flightService.findByFltNoAndFltDateAndTime("6E-6686", date, time);
        log.info(flt.toString());
        date = sdf.parse("22/08/2020");
        time = stf.parse("06:15");
        flt.setFltDate(date);
        flt.setFlightTime(time);

        flightService.save(flt);
    }

}