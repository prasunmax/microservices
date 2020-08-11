package prasun.springboot.airline.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import prasun.springboot.airline.entity.AirlineInfo;
import prasun.springboot.airline.repository.AirlineInfoRepository;
import prasun.springboot.airline.repository.AirlineInfoSearchRepository;

@Service
public class AirlineInfoService {
	private static final Logger log = LoggerFactory.getLogger(AirlineInfoService.class);
	private AirlineInfoSearchRepository searchRepo;
	private AirlineInfoRepository repo;

	@Autowired
	public AirlineInfoService(AirlineInfoSearchRepository searchRepo, AirlineInfoRepository repo) {
		super();
		this.searchRepo = searchRepo;
		this.repo = repo;
	}

	public AirlineInfo findById(int id) {
		return searchRepo.findById(id);
	}

	public List<AirlineInfo> findAll() {

		return searchRepo.findAll();
	}

	public AirlineInfo findByAirlineName(String name) {
		return searchRepo.findByAirlineName(name);
	}


	public AirlineInfo save(AirlineInfo airlineInfo) {

		log.info("Came to save");
		airlineInfo.setAirlineName(airlineInfo.getAirlineName().toUpperCase()); // Just in case the name is sent in
																				// non-upper case
		return repo.save(airlineInfo);
	}

}
