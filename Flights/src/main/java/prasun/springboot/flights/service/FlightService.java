package prasun.springboot.flights.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import prasun.springboot.flights.VO.FlightsModelVO;
import prasun.springboot.flights.VO.SearchFlightVO;
import prasun.springboot.flights.entity.AirlineInfo;
import prasun.springboot.flights.entity.Fare;
import prasun.springboot.flights.entity.Flight;
import prasun.springboot.flights.entity.FlightInfo;
import prasun.springboot.flights.entity.Inventory;
import prasun.springboot.flights.repository.FlightRepository;

@Service
public class FlightService {

	private FlightRepository repo;
	private AirlineInfoService airlineInfoService;
	private InventoryService inventoryService;
	private FareService fareService;
	private FlightInfoService flightInfoService;
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	public FlightService(FlightRepository repo, AirlineInfoService airlineInfoService,
			InventoryService inventoryService, FareService fareService, FlightInfoService flightInfoService,
			EntityManagerFactory entityManagerFactory) {
		super();
		this.repo = repo;
		this.airlineInfoService = airlineInfoService;
		this.inventoryService = inventoryService;
		this.fareService = fareService;
		this.flightInfoService = flightInfoService;
		this.entityManagerFactory = entityManagerFactory;
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
		return repo.queryByFltDateAndAirline(fltDate, airline.toUpperCase());
	}

	public List<Flight> findByFltDateAndFltNoLike(int month, String flt) {
		return repo.queryByFltDate_MonthAndFltNoLike(month, flt);
	}

	public List<Flight> findByFltDateAndFltNoLike(Date fltDate, String flt) {
		return repo.queryByFltDateAndFltNoLike(fltDate, flt);
	}

	public Flight findByFltDateAndFltNo(Date fltDate, String fltNo) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		// @Query("select f from Flight f where DATE(f.fltDate) = ?1 and
		// DATE(f.flightTime) = ?2")

		Flight flight = entityManager
				.createQuery("select f from Flight f where f.fltDate = :fltDate and f.fltNo = :fltNo", Flight.class)
				.setParameter("fltDate", fltDate).setParameter("fltNo", fltNo).getSingleResult();
		return flight;
	}

	public List<Flight> findByFltDateAndOrigin(Date fltDate, String origin) {
		return repo.queryByFltDateAndOrigin(fltDate, origin.toUpperCase());
	}

	public List<Flight> findByFltDateAndOriginAndDestination(Date fltDate, String origin, String dest) {
		return repo.queryByOriginAndDestinationAndFltDtOrderByFare(fltDate, origin.toUpperCase(), dest.toUpperCase());
	}

	public List<Flight> findByOriginAndDestinationAndFltNo(String origin, String dest, String fltNo) {
		return repo.queryByOriginAndDestinationAndFltNo(origin.toUpperCase(), dest.toUpperCase(), fltNo.toUpperCase());
	}

	public List<Flight> findByFltDateAndTime(Date fltDate, Date fltTime) {
		return repo.queryByFltDateAndFlightTime(fltDate, fltTime);
	}

	public Flight findByFltNoAndFltDateAndTime(String fltNo, Date fltDate, Date fltTime) {
		return repo.queryByFltNoAndFltDateAndFlightTime(fltNo.toUpperCase(), fltDate, fltTime);
	}

	public FlightsModelVO findByFltDateAndOriginLikeAndDestinationLike(SearchFlightVO searchFlight) {
		FlightsModelVO flights = new FlightsModelVO();
		flights.setSearchFlight(searchFlight);
		flights.setFlightList(repo.queryByFltDateAndOriginLikeAndDestinationLike(searchFlight.getFlightDate(),
				searchFlight.getOrigin().toUpperCase() + "%", searchFlight.getDestination().toUpperCase() + "%"));
		return flights;
	}

	public FlightsModelVO findByFltDateAndOriginLikeAndDestinationLikeAndSeat(SearchFlightVO searchFlight) {
		FlightsModelVO flights = new FlightsModelVO();
		flights.setSearchFlight(searchFlight);
		flights.setFlightList(repo.queryByFltDateAndOriginLikeAndDestinationLikeAndSeat(searchFlight.getFlightDate(),
				searchFlight.getOrigin().toUpperCase() + "%", searchFlight.getDestination().toUpperCase() + "%",
				searchFlight.getSeat()));
		return flights;
	}

	@Transactional(rollbackOn = Exception.class)
	public Flight saveFlight(String airlineName, int invCount, String fareCurrency, Double fareVal, String fltNo,
			String fltTyp, String origin, String destination, Date fltDate, Date fltTime, String duration) {
		try {

			// Get the Airline id for an Airline Name
			AirlineInfo airlineInfo = airlineInfoService.findByAirlineName(airlineName.toUpperCase());
			if (null == airlineInfo) {
				throw new RuntimeException("Airline not present please try again.");
			}
			// Save FlightInfo
			FlightInfo fltInfo = flightInfoService.save(fltNo.toUpperCase(), fltTyp.toUpperCase(), invCount,
					airlineInfo);
			Flight flt = null;
			try {
				// Check if the flight is already in that day
				flt = findByFltDateAndFltNo(fltDate, fltNo);
				throw new RuntimeException("Flight alredy exists for the day");
			} catch (NoResultException e) {}

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
		repo.flush();
	}

	public FlightsModelVO getFlightsBasedOnInputParameters(SearchFlightVO searchFlight) {
		FlightsModelVO flights = new FlightsModelVO();
		CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> flight = cq.from(Flight.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (!StringUtils.isEmpty(searchFlight.getOrigin())) {
			predicates.add(cb.like(flight.get("origin"), searchFlight.getOrigin().toUpperCase() + "%"));
		}
		if (!StringUtils.isEmpty(searchFlight.getDestination())) {
			predicates.add(cb.like(flight.get("destination"), searchFlight.getDestination().toUpperCase() + "%"));
		}
		if (null != searchFlight.getFlightDate()) {
			predicates.add(cb.equal(flight.get("fltDate"), searchFlight.getFlightDate()));
		}
		if (null != searchFlight.getSeat()) {
			Join<Flight, Inventory> country = flight.join("inventory");
			predicates.add(cb.greaterThanOrEqualTo(country.get("count"), searchFlight.getSeat()));
		}
		cq.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Flight> query = entityManagerFactory.createEntityManager().createQuery(cq);
		flights.setFlightList(query.getResultList());

		return flights;
	}

	public FlightsModelVO getFlightsBasedOnInputParameters(Optional<String> origin, Optional<String> destination,
			Optional<String> fltNum, Optional<String> fltDate, Optional<Integer> seat) {
		FlightsModelVO flights = new FlightsModelVO();
		CriteriaBuilder cb = entityManagerFactory.getCriteriaBuilder();
		CriteriaQuery<Flight> cq = cb.createQuery(Flight.class);
		Root<Flight> flight = cq.from(Flight.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (origin.isPresent()) {
			predicates.add(cb.like(flight.get("origin"), origin.get().toUpperCase() + "%"));
		}
		if (destination.isPresent()) {
			predicates.add(cb.like(flight.get("destination"), destination.get().toUpperCase() + "%"));
		}
		if (fltNum.isPresent()) {
			predicates.add(cb.like(flight.get("fltNo"), fltNum.get().toUpperCase() + "%"));
		}
		if (fltDate.isPresent()) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date date;
			try {
				date = sdf.parse(fltDate.get());
			} catch (ParseException e) {
				return flights;
			}
			predicates.add(cb.equal(flight.get("fltDate"), date));
		}
		if (seat.isPresent()) {
			Join<Flight, Inventory> country = flight.join("inventory");
			predicates.add(cb.greaterThanOrEqualTo(country.get("count"), seat.get()));
		}
		TypedQuery<Flight> query = entityManagerFactory.createEntityManager().createQuery(cq);
		flights.setFlightList(query.getResultList());

		return flights;
	}
}
