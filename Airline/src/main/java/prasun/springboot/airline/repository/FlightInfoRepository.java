package prasun.springboot.airline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import prasun.springboot.airline.entity.FlightInfo;

public interface FlightInfoRepository extends JpaRepository<FlightInfo, Long> {
    @Query("select f from FlightInfo f where f.fltNo=?1")
	FlightInfo queryByFltNo(String fltNo);
}
