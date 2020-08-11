package prasun.springboot.airline.controller;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import prasun.springboot.airline.service.AirlineInfoService;

@Controller
public class Receiver {
	private static final Logger log = LoggerFactory.getLogger(Receiver.class);
	
	@Autowired
	private AirlineInfoService searchService;
	
	@Bean
	Queue queue() {
		return new Queue("AirLineQ", false);
	}
	
	@RabbitListener(queues = "AirLineQ")
    public void processMessage(Map<String,String> airlineInfoReq) {
        log.info("===========> ==== <===========");
        log.info(airlineInfoReq.get("airline"));
        log.info("===========> ==== <===========");
        log.info("The Airline Details for this flight is:"+searchService.findByAirlineName(airlineInfoReq.get("airline")));
       //This call is just for validation of Rabbit MQ
    }		
}
