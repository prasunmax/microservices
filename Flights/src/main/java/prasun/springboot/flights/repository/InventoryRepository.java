package prasun.springboot.flights.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prasun.springboot.flights.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
