package prasun.springboot.flights.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import prasun.springboot.flights.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {
	
	@Query("select f from Flight f where UCASE(f.origin) = ?1 and UCASE(f.destination) = ?2 and UCASE(f.fltNo) = ?3")
    List<Flight> queryByOriginAndDestinationAndFltNo(String origin, String dest, String fltNo);

	@Query("select f from Flight f where DATE(f.fltDate) = ?1 and DATE(f.flightTime) = ?2")
    List<Flight> queryByFltDateAndFlightTime(Date fltDate, Date fltTime);
	
	
	@Query("select f from Flight f where UCASE(f.fltNo) = ?1 and DATE(f.fltDate) = ?2 and DATE(f.flightTime) = ?3")
    Flight queryByFltNoAndFltDateAndFlightTime(String fltNo, Date fltDate, Date fltTime);

	@Query("select f from Flight f where f.fltDate = ?1 and UCASE(f.origin) like ?2 and UCASE(f.destination) like ?3")
	List<Flight> queryByFltDateAndOriginLikeAndDestinationLike(Date fltDate, String origin, String dest);

    @Query("select f from Flight f join Inventory iv on f.inventory.id=iv.id " +
            "where f.fltDate = ?1 and f.origin like ?2 and f.destination like ?3 and iv.count>= ?4 " +
            " order by f.flightTime")
    List<Flight> queryByFltDateAndOriginLikeAndDestinationLikeAndSeat(Date fltDate, String origin, String dest, int seat);

    @Query("select f from Flight f join Fare fr on f.fare.id=fr.id " +
            "where f.fltDate = ?1 and f.origin = ?2 and f.destination = ?3 " +
            " order by fr.fare")
    List<Flight> queryByOriginAndDestinationAndFltDtOrderByFare(Date fltDate, String origin, String dest);


    @Query("select f from Flight f where month(f.fltDate) = ?1 and f.fltNo like ?2")
    List<Flight> queryByFltDate_MonthAndFltNoLike(int month, String Flt);

    @Query("select f from Flight f where DATE(f.fltDate) = ?1 and f.fltNo like ?2")
    List<Flight> queryByFltDateAndFltNoLike(Date fltDate, String flt);


    @Query("select f from Flight f " +
            "join FlightInfo fi on f.flightInfo.id=fi.id " +
            "join AirlineInfo ai on ai.id=fi.airlineInfo.id where DATE(f.fltDate) = ?1 and UCASE(ai.airlineName) = ?2")
    List<Flight> queryByFltDateAndAirline(Date fltDate, String airline);

    @Query("select f from Flight f where DATE(f.fltDate) = ?1 and UCASE(f.origin) = ?2")
    List<Flight> queryByFltDateAndOrigin(Date fltDate, String origin);

    @Query("select f from Flight f where DATE(f.fltDate) = ?1 and UCASE(f.origin) = ?2 and UCASE(f.destination) = ?3")
    List<Flight> queryByFltDateAndOriginAndDestination(Date fltDate, String origin, String dest);


    @Query("select f from Flight f where UCASE(f.fltNo) = ?1 AND f.fltDate = ?2")
    Flight queryByFltNoAndFltDate(String fltNo, Date fltDate);

}
