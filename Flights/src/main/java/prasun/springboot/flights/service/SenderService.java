package prasun.springboot.flights.service;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class SenderService {
	
	@Autowired
	private RabbitMessagingTemplate template;
	
	@Bean
	Queue queue() {
		return new Queue("AirLineQ", false);
	}
	
	public void send(Object map){
		template.convertAndSend("AirLineQ", map);
	}

}
