package prasun.springboot.flights.VO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import prasun.springboot.flights.entity.Flight;

@Getter
@Setter
public class FlightVO {
	private String message;
	private List<Flight> flights;
}
