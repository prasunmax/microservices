package prasun.springboot.flights.controller;

import java.text.ParseException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import prasun.springboot.flights.VO.FlightSaveVO;
import prasun.springboot.flights.VO.FlightVO;
import prasun.springboot.flights.VO.FlightsModelVO;
import prasun.springboot.flights.VO.SearchFlightVO;
import prasun.springboot.flights.entity.Flight;
import prasun.springboot.flights.service.FlightService;

@RestController
@RequestMapping("/flight")
public class FlightController {
	private static final Logger log = LoggerFactory.getLogger(FlightController.class);

	FlightService flightService;

	@Autowired
	public FlightController(FlightService flightService) {
		super();
		this.flightService = flightService;
	}

	@GetMapping(path = "/sample")
	public SearchFlightVO getMapping() {
		SearchFlightVO flightData = new SearchFlightVO();

		return flightData;
	}

	@PostMapping(path = "/saveflight")
	public ResponseEntity<?> restMain(@RequestBody FlightSaveVO flightData, BindingResult result) throws ParseException {
		if(result.hasErrors()) {
			ResponseEntity.unprocessableEntity().body(flightData);
		}  
		Flight flt = flightService.saveFlight(flightData);
		return ResponseEntity.ok().body(flt);
	}

	@GetMapping(path = "/getflight")
	public ResponseEntity<?> restMain(@RequestBody Optional<SearchFlightVO> flightData, @RequestParam Optional<String> origin,
			@RequestParam Optional<String> destination, @RequestParam Optional<String> fltDate,
			@RequestParam Optional<String> fltNum, @RequestParam Optional<Integer> seat) {
		FlightVO flightVO = new FlightVO();
		FlightsModelVO sndFlightData = null;
		if (flightData.isPresent()) {
			sndFlightData = flightService.getFlightsBasedOnInputParameters(flightData.get());
		} else if (origin.isPresent() || destination.isPresent() || fltDate.isPresent() || fltNum.isPresent()
				|| seat.isPresent()) {
			sndFlightData = flightService.getFlightsBasedOnInputParameters(origin, destination, fltNum, fltDate, seat);
		}else {
			return ResponseEntity.badRequest().build();
		}
		log.info("Flight found");
		flightVO.setMessage("Success");
		flightVO.setFlights(sndFlightData.getFlightList());
		return ResponseEntity.ok(flightVO);
	}

}