package prasun.springboot.flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prasun.springboot.flights.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {
	
	

}
