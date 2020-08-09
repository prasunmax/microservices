package prasun.springboot.flights.VO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AirlineVO {
	@NotBlank(message = "Should not be blank")
	@NotNull(message = "Should be present")
	String airlineName;
	
	String logo;
}
