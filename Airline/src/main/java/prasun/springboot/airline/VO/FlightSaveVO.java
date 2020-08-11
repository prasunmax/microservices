package prasun.springboot.airline.VO;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;
import prasun.springboot.airline.CustomJsonDateDeserializer;

@Data
public class FlightSaveVO {
	@NotNull(message = "Should have a value")
	String airlineName;
	int invCount;
	@NotNull(message = "Should have a value")
	String fareCurrency;
	@NotNull(message = "Should have a value")
	Double fareVal;
	@NotNull(message = "Should have a value")
	String fltNo;
	@NotNull(message = "Should have a value")
	String fltTyp;
	@NotNull(message = "Should have a value")
	String origin;
	@NotNull(message = "Should have a value")
	String destination;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull(message = "Should have a value")
	Date fltDate;
	@NotNull(message = "Should have a value")
	String fltTime;
	@NotNull(message = "Should have a value")
	String duration;
	
	@JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setFltDate(Date flightDate) {
    	this.fltDate = flightDate;		
	}
}
