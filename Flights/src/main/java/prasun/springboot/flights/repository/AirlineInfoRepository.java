package prasun.springboot.flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prasun.springboot.flights.entity.AirlineInfo;

public interface AirlineInfoRepository extends JpaRepository<AirlineInfo, Long> {
}
