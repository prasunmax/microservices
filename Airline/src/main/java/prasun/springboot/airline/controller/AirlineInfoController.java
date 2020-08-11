package prasun.springboot.airline.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import prasun.springboot.airline.VO.AirlineVO;
import prasun.springboot.airline.entity.AirlineInfo;
import prasun.springboot.airline.service.AirlineInfoService;

@RestController
public class AirlineInfoController {

	private AirlineInfoService service;

	@Autowired
	public AirlineInfoController(AirlineInfoService service) {
		super();
		this.service = service;
	}

	@GetMapping("/rest/getAllAirlines")
	public List<AirlineInfo> getAirlines() {
		return service.findAll();
	}
	@GetMapping("/rest/getAirlineByName")
	public ResponseEntity<?> getAirlineByName(@Valid @RequestBody AirlineVO airline,BindingResult result) {
		AirlineInfo info =  service.findByAirlineName(airline.getAirlineName());
		return ResponseEntity.ok(info);
	}

	@PostMapping("/rest/getAirlineByName")
	public ResponseEntity<?> getAirlineByNameForPost(@Valid @RequestBody AirlineVO airline,BindingResult result) {
		AirlineInfo info =  service.findByAirlineName(airline.getAirlineName());
		return ResponseEntity.ok(info);
	}
	
	@PostMapping("/rest/saveAirline")
	public ResponseEntity<?> saveAirline(@Valid @RequestBody AirlineVO airlineName) {
		AirlineInfo airlineInfo = new AirlineInfo(airlineName.getLogo(),airlineName.getAirlineName());		
		try {
			airlineInfo = service.save(airlineInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(airlineInfo);
		}
		return ResponseEntity.ok(airlineInfo);
	}
}
