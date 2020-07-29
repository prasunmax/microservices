package prasun.springboot.flights.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "flight")
public class Flight {

    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    @GeneratedValue(generator = "idGenerator")
    @Id
    private Long id;

    @Basic
    @Column(name = "destination")
    private String destination;

    @Basic
    @Column(name = "duration")
    private String duration;

    @Basic
    @Column(name = "flight_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fltDate;

    @Basic
    @Column(name = "flight_number")
    private String fltNo;

    @Basic
    @Column(name = "flight_time")
    @Temporal(TemporalType.TIME)
    private Date flightTime;

    @Basic
    @Column(name = "origin")
    private String origin;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fare_id")
    private Fare fare;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "flight_infoid")
    private FlightInfo flightInfo;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "inv_id")
    private Inventory inventory;


    public Flight(String destination, String duration, Date fltDate, String fltNo, Date flightTime, String origin,
                  Fare fare, FlightInfo flightInfo, Inventory inventory) {
		super();
		this.destination = destination;
		this.duration = duration;
		this.fltDate = fltDate;
		this.fltNo = fltNo;
		this.flightTime = flightTime;
		this.origin = origin;
		this.fare = fare;
		this.flightInfo = flightInfo;
		this.inventory = inventory;
	}
}
