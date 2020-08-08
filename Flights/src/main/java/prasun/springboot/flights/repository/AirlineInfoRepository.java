package prasun.springboot.flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prasun.springboot.flights.entity.AirlineInfo;

public interface AirlineInfoRepository extends JpaRepository<AirlineInfo, Long> {

	//Other than Save no other use of this , eventually I will port all the queries here 

}
