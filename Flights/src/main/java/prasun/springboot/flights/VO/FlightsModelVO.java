package prasun.springboot.flights.VO;

import java.util.List;

import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;
import prasun.springboot.flights.entity.Flight;

@Setter
@Getter
public class FlightsModelVO {
    @Valid
    private SearchFlightVO searchFlight;

    private List<Flight> flightList;
    
    private Long flightId;
    
}
