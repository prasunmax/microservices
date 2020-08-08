package prasun.springboot.flights.VO;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Getter;
import lombok.Setter;
import prasun.springboot.flights.CustomJsonDateDeserializer;

@Setter
@Getter
public class SearchFlightVO {

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
    private Date flightDate;
    @Size(min = 3,message = "Enter at-least 3 characters")
    private String  origin;
    @Size(min = 3,message = "Enter at-least 3 characters")
    private String destination;
    
    @Max(value = 4,message="Maximum 4 seats can be booked at a time.")
    @Min(value = 1,message="Maximum 1 seat should be selected.")
    private Integer seat;
    
    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setFlightDate(Date flightDate) {
    	this.flightDate = flightDate;		
	}
}
