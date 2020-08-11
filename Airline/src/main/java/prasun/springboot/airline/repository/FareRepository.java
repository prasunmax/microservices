package prasun.springboot.airline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prasun.springboot.airline.entity.Fare;

public interface FareRepository extends JpaRepository<Fare, Long> {
}
