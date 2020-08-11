package prasun.springboot.airline.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import prasun.springboot.airline.VO.FlightSaveVO;
import prasun.springboot.airline.VO.FlightsModelVO;
import prasun.springboot.airline.VO.SearchFlightVO;
import prasun.springboot.airline.entity.AirlineInfo;
import prasun.springboot.airline.entity.Fare;
import prasun.springboot.airline.entity.Flight;
import prasun.springboot.airline.entity.FlightInfo;
import prasun.springboot.airline.entity.Inventory;
import prasun.springboot.airline.repository.FlightRepository;
import prasun.springboot.airline.repository.FlightSearchRepository;

@Service
public class FlightService {
	private static final Logger log = LoggerFactory.getLogger(FlightService.class);
	private FlightRepository repo;
	private AirlineInfoService airlineInfoService;
	private InventoryService inventoryService;
	private FareService fareService;
	private FlightInfoService flightInfoService;
	private FlightSearchRepository flightSearchRepository;

	@Autowired
	public FlightService(FlightRepository repo, AirlineInfoService airlineInfoService,
			InventoryService inventoryService, FareService fareService, FlightInfoService flightInfoService,
			FlightSearchRepository flightSearchRepository) {
		super();
		this.repo = repo;
		this.airlineInfoService = airlineInfoService;
		this.inventoryService = inventoryService;
		this.fareService = fareService;
		this.flightInfoService = flightInfoService;
		this.flightSearchRepository = flightSearchRepository;
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
			AirlineInfo airlineInfo;
			try {
				airlineInfo = airlineInfoService.findByAirlineName(airlineName.toUpperCase());
			} catch (NoResultException e) {
				throw new RuntimeException("Airline not present please try again.");
			}
			// Save FlightInfo
			FlightInfo fltInfo = flightInfoService.save(fltNo.toUpperCase(), fltTyp.toUpperCase(), invCount,
					airlineInfo);
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

		return flightSearchRepository.getFlightsBasedOnInputParameters(origin, destination, fltNum, fltDate, seat);
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
