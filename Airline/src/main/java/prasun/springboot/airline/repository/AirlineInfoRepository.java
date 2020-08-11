package prasun.springboot.airline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import prasun.springboot.airline.entity.AirlineInfo;

@Repository
public interface AirlineInfoRepository extends JpaRepository<AirlineInfo, Long> {
}
