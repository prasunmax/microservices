package prasun.springboot.flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prasun.springboot.flights.entity.Fare;

public interface FareRepository extends JpaRepository<Fare, Long> {
}
