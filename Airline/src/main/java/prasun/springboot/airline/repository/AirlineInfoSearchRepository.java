package prasun.springboot.airline.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import prasun.springboot.airline.entity.AirlineInfo;

@Repository
public class AirlineInfoSearchRepository {
	private EntityManagerFactory entityManagerFactory;

	@Autowired
	public AirlineInfoSearchRepository(EntityManagerFactory entityManagerFactory) {
		super();
		this.entityManagerFactory = entityManagerFactory;
	}

	public AirlineInfo findById(int id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		AirlineInfo airline = entityManager.find(AirlineInfo.class, id);
		return airline;
	}

	public List<AirlineInfo> findAll() {

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		List<AirlineInfo> airlines = entityManager.createQuery("select a from AirlineInfo a", AirlineInfo.class)
				.getResultList();
		return airlines;
	}

	public AirlineInfo findByAirlineName(String name) {

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		AirlineInfo airline = entityManager
				.createQuery("select a from AirlineInfo a where a.airlineName = :name", AirlineInfo.class)
				.setParameter("name", name).getSingleResult();
		return airline;

	}

}
