package prasun.springboot.airline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import prasun.springboot.airline.entity.Fare;
import prasun.springboot.airline.repository.FareRepository;

@Service
public class FareService {

    private FareRepository repo;

	@Autowired
    public FareService(FareRepository repo) {
		super();
		this.repo = repo;
	}

	public List<Fare> findAll() {
        return repo.findAll();
    }

    public Fare save(Fare fare) {
        return repo.save(fare);
    }

    public Fare save(String fareCurrency, Double fareVal) {
        Fare fare = new Fare();
        fare.setCurrency(fareCurrency);
        fare.setFare(fareVal);
        return repo.save(fare);
    }
}
