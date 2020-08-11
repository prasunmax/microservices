package prasun.springboot.airline.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import prasun.springboot.airline.entity.Inventory;
import prasun.springboot.airline.repository.InventoryRepository;

@Service
public class InventoryService {

    private InventoryRepository repo;
 
    @Autowired
    public InventoryService(InventoryRepository repo) {
		super();
		this.repo = repo;
	}	

    public List<Inventory> findAll() {
        return repo.findAll();
    }
    public Inventory findById(Long id) {
        return repo.findById(id).get();
    }

    @Transactional
    public Inventory save(Inventory inv) {
        return repo.save(inv);
    }

    public Inventory save(int invCount) {
        Inventory inv = new Inventory();
        inv.setCount(invCount);
        return repo.save(inv);
    }
}
