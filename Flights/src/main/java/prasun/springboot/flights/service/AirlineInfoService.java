package prasun.springboot.flights.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import prasun.springboot.flights.entity.AirlineInfo;
import prasun.springboot.flights.repository.AirlineInfoRepository;

@Service
public class AirlineInfoService {
	private AirlineInfoRepository repo;
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	public AirlineInfoService(AirlineInfoRepository repo, EntityManagerFactory entityManagerFactory) {
		super();
		this.repo = repo;
		this.entityManagerFactory = entityManagerFactory;
	}

	public List<AirlineInfo> findAll() {
		return repo.findAll();
	}

	public AirlineInfo findByAirlineName(String name) {
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		AirlineInfo  airline =  entityManager.createQuery("select a from AirlineInfo a where a.airlineName = :name", AirlineInfo.class).setParameter("name", name).getSingleResult();
		return airline;
		 
		//return repo.queryByAirlineName(name);
	}

	public Optional<AirlineInfo> findByAirlineId(Long airId) {
		return repo.findById(airId);
	}

	public AirlineInfo save(AirlineInfo airlineInfo)  {
		
		/*
		 * EntityManager entityManager = entityManagerFactory.createEntityManager();
		 * entityManager.getTransaction().begin(); entityManager.persist(airlineInfo);
		 * entityManager.getTransaction().commit(); return airlineInfo;
		 */
		airlineInfo.setAirlineName(airlineInfo.getAirlineName().toUpperCase()); //Just in case the name is sent in non-upper case
		return repo.save(airlineInfo); 
	}

}
