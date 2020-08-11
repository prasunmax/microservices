package prasun.springboot.airline.VO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import prasun.springboot.airline.entity.Flight;

@Getter
@Setter
public class FlightVO {
	private String message;
	private List<Flight> flights;
}
