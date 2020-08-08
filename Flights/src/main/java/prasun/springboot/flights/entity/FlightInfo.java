package prasun.springboot.flights.entity;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flight_info")
public class FlightInfo extends GenericEntity{

	@Basic
	@Column(name = "flight_number", unique = true)
	private String fltNo;

	@Basic
	@Column(name = "flight_type")
	private String fltTyp;

	@Basic
	@Column(name = "numberof_seats", nullable = false)
	private Integer noOfSeats;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "flights_info", joinColumns = {
			@JoinColumn(referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "id") })
	private AirlineInfo airlineInfo;

	public FlightInfo(String fltNo, String fltTyp, Integer noOfSeats) {
		super();
		this.fltNo = fltNo;
		this.fltTyp = fltTyp;
		this.noOfSeats = noOfSeats;
	}

}
