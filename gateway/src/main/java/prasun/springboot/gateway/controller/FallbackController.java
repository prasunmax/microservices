package prasun.springboot.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
	
	@RequestMapping("/airlineFallback")
	public Mono<String> airlineFallback(){
		return Mono.just("Airline service is taking too long to respond or is down. Please try again later.");
	}
	
	@RequestMapping("/flightFallback")
	public Mono<String> flightFallback(){
		return Mono.just("Flight service is taking too long to respond or is down. Please try again later.");
	}

}
