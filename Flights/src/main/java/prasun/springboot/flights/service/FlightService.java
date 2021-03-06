package prasun.springboot.flights.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.collections4.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import prasun.springboot.flights.VO.AirlineVO;
import prasun.springboot.flights.VO.FlightSaveVO;
import prasun.springboot.flights.VO.FlightsModelVO;
import prasun.springboot.flights.VO.SearchFlightVO;
import prasun.springboot.flights.entity.AirlineInfo;
import prasun.springboot.flights.entity.Fare;
import prasun.springboot.flights.entity.Flight;
import prasun.springboot.flights.entity.FlightInfo;
import prasun.springboot.flights.entity.Inventory;
import prasun.springboot.flights.repository.FlightRepository;
import prasun.springboot.flights.repository.FlightSearchRepository;

@Service
@RefreshScope
public class FlightService {
	private static final Logger log = LoggerFactory.getLogger(FlightService.class);
	private FlightRepository repo;
	private InventoryService inventoryService;
	private FareService fareService;
	private FlightInfoService flightInfoService;
	private FlightSearchRepository flightSearchRepository;
    private RestTemplate template;
    private SenderService sender;
    
    //private"http://airline-service/airline/getAirlineByName"
    @Value("microservices.endpoints.endpoint.airline.getAirlineByName")
    private String AIRLINE_URL;

	@Autowired
	public FlightService(FlightRepository repo, InventoryService inventoryService, FareService fareService, 
			FlightInfoService flightInfoService,RestTemplate template, SenderService sender,
			FlightSearchRepository flightSearchRepository) {
		super();
		this.repo = repo;
		this.inventoryService = inventoryService;
		this.fareService = fareService;
		this.flightInfoService = flightInfoService;
		this.flightSearchRepository = flightSearchRepository;
		this.template = template;
		this.sender = sender;
	}

	public List<Flight> findAll() {
		return repo.findAll();
	}

	public Flight findFlightById(Long id) {
		return repo.findById(id).get();
	}

	public void updateInventory(String fltNo, Date fltDate, int size) {
		// Get flight details
		Flight flt = findByFltDateAndFltNo(fltDate, fltNo);
		// get Inventory details
		Inventory inventory = flt.getInventory();

		inventory.setCount(inventory.getCount() - size);
		inventoryService.save(inventory);
	}

	public List<Flight> findByFltDateAndAirline(Date fltDate, String airline) {
		return flightSearchRepository.queryByFltDateAndAirline(fltDate, airline.toUpperCase());
	}

	public List<Flight> findByFltDateAndFltNoLike(Date fltDate, String flt) {
		return flightSearchRepository.queryByFltDateAndFltNoLike(fltDate, flt);
	}

	public Flight findByFltDateAndFltNo(Date fltDate, String fltNo) {
		return flightSearchRepository.queryByFltNoAndFltDate(fltNo, fltDate);
	}

	public List<Flight> findByFltDateAndOrigin(Date fltDate, String origin) {
		return flightSearchRepository.queryByFltDateAndOrigin(fltDate, origin.toUpperCase());
	}

	public List<Flight> findByFltDateAndOriginAndDestination(Date fltDate, String origin, String dest) {
		return flightSearchRepository.queryByOriginAndDestinationAndFltDtOrderByFare(fltDate, origin.toUpperCase(),
				dest.toUpperCase());
	}

	public List<Flight> findByOriginAndDestinationAndFltNo(String origin, String dest, String fltNo) {
		return flightSearchRepository.queryByOriginAndDestinationAndFltNo(origin.toUpperCase(), dest.toUpperCase(),
				fltNo.toUpperCase());
	}

	public List<Flight> findByFltDateAndTime(Date fltDate, Date fltTime) {
		return flightSearchRepository.queryByFltDateAndFlightTime(fltDate, fltTime);
	}

	public Flight findByFltNoAndFltDateAndTime(String fltNo, Date fltDate, Date fltTime) {
		return flightSearchRepository.queryByFltNoAndFltDateAndFlightTime(fltNo.toUpperCase(), fltDate, fltTime);
	}

	public FlightsModelVO findByFltDateAndOriginLikeAndDestinationLike(SearchFlightVO searchFlight) {
		FlightsModelVO flights = new FlightsModelVO();
		flights.setSearchFlight(searchFlight);
		flights.setFlightList(flightSearchRepository.queryByFltDateAndOriginLikeAndDestinationLike(
				searchFlight.getFlightDate(), searchFlight.getOrigin().toUpperCase() + "%",
				searchFlight.getDestination().toUpperCase() + "%"));
		return flights;
	}

	public FlightsModelVO findByFltDateAndOriginLikeAndDestinationLikeAndSeat(SearchFlightVO searchFlight) {
		FlightsModelVO flights = new FlightsModelVO();
		flights.setSearchFlight(searchFlight);
		flights.setFlightList(flightSearchRepository.queryByFltDateAndOriginLikeAndDestinationLikeAndSeat(
				searchFlight.getFlightDate(), searchFlight.getOrigin().toUpperCase() + "%",
				searchFlight.getDestination().toUpperCase() + "%", searchFlight.getSeat()));
		return flights;
	}

	@Transactional(rollbackOn = Exception.class)
	public Flight saveFlight(String airlineName, int invCount, String fareCurrency, Double fareVal, String fltNo,
			String fltTyp, String origin, String destination, Date fltDate, Date fltTime, String duration) {
		try {

			// Get the Airline id for an Airline Name
			//airlineInfo = airlineInfoService.findByAirlineName(airlineName.toUpperCase());
			AirlineVO airlineVo = new AirlineVO();
			airlineVo.setAirlineName(airlineName);
		    HttpEntity<Object> req = 
		    	      new HttpEntity<Object>(airlineVo);
			ResponseEntity<AirlineInfo>	airlineInfo = template.exchange("http://airline-service/airline/getAirlineByName",HttpMethod.POST, req, AirlineInfo.class);
			//Send a message
			Map<String, String> map = new HashedMap<>();
			map.put("airline", airlineVo.getAirlineName());
			sender.send(map);
			// Save FlightInfo if not already exists
			//TODO fix this
			FlightInfo fltInfo = flightInfoService.save(fltNo.toUpperCase(), fltTyp.toUpperCase(), invCount,
					airlineInfo.getBody());
			// Check if the flight is already in that day
			Flight flt = findByFltDateAndFltNo(fltDate, fltNo);
			if(null!= flt) {
				log.info("Flight Already Exists.");
				throw new RuntimeException("Flight alredy exists for the day");
			}

			// Save inventory
			Inventory inv = inventoryService.save(invCount);

			// Save Fare
			Fare fare = fareService.save(fareCurrency.toUpperCase(), fareVal);

			// Now Save Flight
			flt = new Flight(destination.toUpperCase(), duration, fltDate, fltNo, fltTime, origin, fare, fltInfo, inv);
			flt = repo.save(flt);
			return flt;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Transactional(rollbackOn = Exception.class)
	public void save(Flight flt) {
		repo.save(flt);
	}

	public FlightsModelVO getFlightsBasedOnInputParameters(SearchFlightVO searchFlight) {
		return flightSearchRepository.getFlightsBasedOnInputParameters(searchFlight);
	}

	public FlightsModelVO getFlightsBasedOnInputParameters(Optional<String> origin, Optional<String> destination,
			Optional<String> fltNum, Optional<String> fltDate, Optional<Integer> seat) {
		if(seat.isPresent())
			return flightSearchRepository.getFlightsBasedOnInputParameters(origin, destination, fltNum, fltDate, seat);
		else
			return flightSearchRepository.getFlightsBasedOnInputParameters(origin, destination, fltNum, fltDate);
	}

	public Flight saveFlight(FlightSaveVO flightData) throws ParseException {
		/*
		 * The function accepts the below values String airlineName, int invCount,
		 * String fareCurrency, Double fareVal, String fltNo, String fltTyp, String
		 * origin, String destination, Date fltDate, Date fltTime, String duration
		 */
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm");
		Date time = stf.parse(flightData.getFltTime());
		return saveFlight(flightData.getAirlineName(), flightData.getInvCount(), flightData.getFareCurrency(),
				flightData.getFareVal(), flightData.getFltNo(), flightData.getFltTyp(), flightData.getOrigin(),
				flightData.getDestination(), flightData.getFltDate(), time,
				flightData.getDuration());

	}
}
