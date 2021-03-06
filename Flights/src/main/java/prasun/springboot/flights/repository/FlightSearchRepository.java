package prasun.springboot.flights.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.ogm.OgmSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import prasun.springboot.flights.VO.FlightsModelVO;
import prasun.springboot.flights.VO.SearchFlightVO;
import prasun.springboot.flights.entity.Flight;
import prasun.springboot.flights.entity.Inventory;

@Repository
public class FlightSearchRepository {
	private static final Logger log = LoggerFactory.getLogger(FlightSearchRepository.class);
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	public FlightSearchRepository(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	// @Query("select f from Flight f where UCASE(f.origin) = ?1 and
	// UCASE(f.destination) = ?2 and UCASE(f.fltNo) = ?3")
	public List<Flight> queryByOriginAndDestinationAndFltNo(String origin, String dest, String fltNo) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<Flight> flights = entityManager.createQuery(
				"select f from Flight f where f.origin = :origin and f.destination  = :dest and f.fltNo = :fltNo",
				Flight.class).setParameter("origin", origin).setParameter("dest", dest).setParameter("fltNo", fltNo)
				.getResultList();
		return flights;
	}

	// @Query("select f from Flight f where DATE(f.fltDate) = ?1 and
	// DATE(f.flightTime) = ?2")
	public List<Flight> queryByFltDateAndFlightTime(Date fltDate, Date fltTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<Flight> flights = entityManager
				.createQuery("select f from Flight f where f.fltDate = :fltDate and f.flightTime = :fltTime",
						Flight.class)
				.setParameter("fltDate", fltDate).setParameter("fltTime", fltTime).getResultList();
		return flights;
	}

	// @Query("select f from Flight f where UCASE(f.fltNo) = ?1 and DATE(f.fltDate)
	// = ?2 and DATE(f.flightTime) = ?3")
	public Flight queryByFltNoAndFltDateAndFlightTime(String fltNo, Date fltDate, Date fltTime) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Flight flight = entityManager.createQuery(
				"select f from Flight f where f.fltNo = :fltNo and f.fltDate = :fltDate and f.flightTime = :fltTime",
				Flight.class).setParameter("fltNo", fltNo).setParameter("fltDate", fltDate)
				.setParameter("fltTime", fltTime).getSingleResult();
		return flight;
	}

	// @Query("select f from Flight f where f.fltDate = ?1 and UCASE(f.origin) like
	// ?2 and UCASE(f.destination) like ?3")
	public List<Flight> queryByFltDateAndOriginLikeAndDestinationLike(Date fltDate, String origin, String dest) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<Flight> flights = entityManager.createQuery(
				"select f from Flight f where f.origin = :origin and f.destination  = :dest and f.fltDate = :fltDate",
				Flight.class).setParameter("origin", origin).setParameter("dest", dest).setParameter("fltDate", fltDate)
				.getResultList();
		return flights;
	}

	// @Query("select f from Flight f join Inventory iv on f.inventory.id=iv.id
	// where f.fltDate = ?1 and f.origin like ?2 and f.destination like ?3 and
	// iv.count>= ?4 order by f.flightTime")
	public List<Flight> queryByFltDateAndOriginLikeAndDestinationLikeAndSeat(Date fltDate, String origin, String dest,
			int seat) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<Flight> flights = entityManager.createQuery(
				"select f from Flight f join Inventory iv on f.inventory.id=iv.id where f.fltDate = :fltDate "
						+ "and f.origin like :origin and f.destination like :dest and iv.count>= :seat order by f.flightTime",
				Flight.class).setParameter("origin", origin).setParameter("dest", dest).setParameter("fltDate", fltDate)
				.setParameter("seat", seat).getResultList();
		return flights;
	}

	// @Query("select f from Flight f join Fare fr on f.fare.id=fr.id where
	// f.fltDate = ?1 and f.origin = ?2 and f.destination = ?3 order by fr.fare")
	public List<Flight> queryByOriginAndDestinationAndFltDtOrderByFare(Date fltDate, String origin, String dest) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<Flight> flights = entityManager
				.createQuery("select f from Flight f join Fare fr on f.fare.id=fr.id where f.origin = :origin and "
						+ "f.destination  = :dest and f.fltDate = :fltDate order by fr.fare", Flight.class)
				.setParameter("origin", origin).setParameter("dest", dest).setParameter("fltDate", fltDate)
				.getResultList();
		return flights;
	}

	// @Query("select f from Flight f where DATE(f.fltDate) = ?1 and f.fltNo like
	// ?2")
	public List<Flight> queryByFltDateAndFltNoLike(Date fltDate, String flt) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<Flight> flights = entityManager
				.createQuery("select f from Flight f where f.fltNo like :flt and f.fltDate = :fltDate", Flight.class)
				.setParameter("flt", flt).setParameter("fltDate", fltDate).getResultList();
		return flights;
	}

//    @Query("select f from Flight f " +
//            "join FlightInfo fi on f.flightInfo.id=fi.id " +
//            "join AirlineInfo ai on ai.id=fi.airlineInfo.id where DATE(f.fltDate) = ?1 and UCASE(ai.airlineName) = ?2")
	public List<Flight> queryByFltDateAndAirline(Date fltDate, String airline) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<Flight> flights = entityManager.createQuery(
				"select f from Flight f join FlightInfo fi on f.flightInfo.id=fi.id "
						+ " join AirlineInfo ai on ai.id=fi.airlineInfo.id where  f.fltDate = :fltDate and ai.airlineName = :airline",
				Flight.class).setParameter("airline", airline).setParameter("fltDate", fltDate).getResultList();
		return flights;
	}

	// @Query("select f from Flight f where DATE(f.fltDate) = ?1 and UCASE(f.origin)
	// = ?2")
	public List<Flight> queryByFltDateAndOrigin(Date fltDate, String origin) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<Flight> flights = entityManager
				.createQuery("select f from Flight f where f.origin = :origin and f.fltDate = :fltDate", Flight.class)
				.setParameter("origin", origin).setParameter("fltDate", fltDate).getResultList();
		return flights;
	}

	// @Query("select f from Flight f where UCASE(f.fltNo) = ?1 AND f.fltDate = ?2")
	public Flight queryByFltNoAndFltDate(String fltNo, Date fltDate) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		// @Query("select f from Flight f where DATE(f.fltDate) = ?1 and
		// DATE(f.flightTime) = ?2")

		List<Flight> flight = entityManager
				.createQuery("select f from Flight f where f.fltDate = :fltDate and f.fltNo = :fltNo", Flight.class)
				.setParameter("fltDate", fltDate).setParameter("fltNo", fltNo).getResultList();
		return flight.size() > 0 ? flight.get(0) : null;
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

		String query = "";
		FlightsModelVO flights = new FlightsModelVO();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		OgmSession ogmSession = entityManager.unwrap(OgmSession.class);
		
		//Session session = entityManager.openSession();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		//query = "select f from Flight f inner join Inventory inv on f.inventory.id=inv.id where 1=1 ";
		//query = "select f from Flight f inner join f.inventory inv where 1=:value ";
		List<String> predicates = new ArrayList<String>();
		
		query = "db.flight.aggregate([{ '$lookup': {" + 
				"  'from': 'inventory'," + 
				"  'localField': 'inventory_id'," + 
				"  'foreignField': '_id'," + 
				"  as: 'inv'" + 
				"} }, { '$unwind': {" + 
				"  'path': '$inv'" + 
				"} }, { '$match': {" + 
				"  'inv.count': { '$gte' :"+seat.get()+" }"; 
//		query = "db.Flight.aggregate([{'$match': {" ;
		if (origin.isPresent()) {
			predicates.add("'origin': '"+ origin.get() +"'");
		}
		if (destination.isPresent()) {
			predicates.add(" 'destination':'"+destination.get()+"'");
		}
		if (fltNum.isPresent()) {
			predicates.add(" 'flight_number':'"+fltNum.get()+"'");
		}
		if (fltDate.isPresent()) {
			try {
				date = sdf.parse(fltDate.get());
			} catch (ParseException e) {
				return flights;
			}
			predicates.add(" 'flight_date':'"+date+"'");
		}
		
		for(String predicate: predicates) {
			query+=" , " + predicate;
		}
		query += " }}])";
		//query = "{ '$and':[ {'origin': 'KOLKATA' } ,{'flight_number':'AI-100'} ]} " ;
		//query = "{ '$and':[ {'origin': 'KOLKATA' } ,{'flight_number':'AI-100'} ]}" ;
		log.info("Query formed is:"+query);
		@SuppressWarnings("unchecked")
		List<Flight> flts = ogmSession.createNativeQuery(query ).addEntity(Flight.class).list();
		
		flights.setFlightList(flts);
		
		//FindIterable<Document> result = collection.aggregate(Arrays.asList(lookup("inventory", "inventory_id", "_id", "inventory"), unwind("$inventory"), match(and(gte("inventory.count", 3L), eq("origin", "KOLKATA")))));
		return flights;
	}
	
	public FlightsModelVO getFlightsBasedOnInputParameters(Optional<String> origin, Optional<String> destination,
			Optional<String> fltNum, Optional<String> fltDate) {
		String query = "";
		FlightsModelVO flights = new FlightsModelVO();
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		//query = "select f from Flight f inner join Inventory inv on f.inventory.id=inv.id where 1=1 ";
		//query = "select f from Flight f inner join f.inventory inv where 1=:value ";
		query = "select f from Flight f where  ";
		List<String> predicates = new ArrayList<String>();
		
		if (origin.isPresent()) {
			predicates.add("  f.origin = :origin");
		}
		if (destination.isPresent()) {
			predicates.add("  f.destination = :destination");
		}
		if (fltNum.isPresent()) {
			predicates.add("  f.fltNo = :fltNum");
		}
		if (fltDate.isPresent()) {
			try {
				date = sdf.parse(fltDate.get());
			} catch (ParseException e) {
				return flights;
			}
			predicates.add("  f.fltDate = :date");
		}
		boolean first = true;
		for(String predicate: predicates) {
			if(first) {
				query+=predicate;
				first = false;
			}else {
				query+=" and " + predicate;
			}
		}
		log.info("Query formed is:"+query);
		TypedQuery<Flight> finalQuery = entityManager.createQuery(query, Flight.class);
		
		//finalQuery.setParameter("value", 1);//1=1 triggers a bug in hql parser (https://hibernate.atlassian.net/browse/HQLPARSER-44): NPE in SingleEntityQueryRendererDelegate.addComparisonPredicate due to null property 

		if (origin.isPresent()) {
			finalQuery.setParameter("origin", origin.get());
		}
		if (destination.isPresent()) {
			finalQuery.setParameter("destination", destination.get());
		}
		if (fltNum.isPresent()) {
			finalQuery.setParameter("fltNum", fltNum.get());
		}
		if (fltDate.isPresent()) {
			finalQuery.setParameter("date", date);
		}
		flights.setFlightList(finalQuery.getResultList());
		return flights;
	}

	public FlightsModelVO getFlightsBasedOnInputParametersbackup(Optional<String> origin, Optional<String> destination,
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
