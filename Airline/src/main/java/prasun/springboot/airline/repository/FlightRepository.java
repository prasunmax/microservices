package prasun.springboot.airline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prasun.springboot.airline.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {
	
	

}
