package prasun.springboot.airline.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import prasun.springboot.airline.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
