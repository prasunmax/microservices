package prasun.springboot.airline.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import prasun.springboot.airline.entity.AirlineInfo;
import prasun.springboot.airline.entity.FlightInfo;
import prasun.springboot.airline.repository.FlightInfoRepository;

@Service
public class FlightInfoService {

	private FlightInfoRepository repo;

	@Autowired
	public FlightInfoService(FlightInfoRepository repo) {
		super();
		this.repo = repo;
	}

	public List<FlightInfo> findAll() {
        return repo.findAll();
    }

    public FlightInfo save(FlightInfo info) {
        FlightInfo fltInfo = repo.save(info);
        return fltInfo;
    }
    public FlightInfo findByFltNo(String fltNo) {
        return repo.queryByFltNo(fltNo);
    }

    public Optional<FlightInfo> findByFltId(Long id) {
        return repo.findById(id);
    }

    public FlightInfo save(String fltNo, String fltTyp, int invCount, AirlineInfo airlineInfo) {
        //Ensure that there are no two records with same flight number
        FlightInfo fltInfo = findByFltNo(fltNo);
        if(null != fltInfo){
            return fltInfo;
        }
        fltInfo = new FlightInfo(fltNo,fltTyp, invCount);
        fltInfo.setAirlineInfo(airlineInfo);
        return repo.save(fltInfo);
    }
}
