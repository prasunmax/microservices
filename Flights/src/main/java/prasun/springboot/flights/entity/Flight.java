package prasun.springboot.flights.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "flight")
public class Flight extends GenericEntity{

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

}
