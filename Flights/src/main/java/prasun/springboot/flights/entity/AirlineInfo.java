package prasun.springboot.flights.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "airline_info")
public class AirlineInfo extends GenericEntity{

	@Basic
	@Column(name = "airline_logo")
	private String logo;

	@Basic
	@Column(name = "name_of_airline")
	private String airlineName;
}
