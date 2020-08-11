package prasun.springboot.airline.entity;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="dd/MM/yyyy")
    private Date fltDate;

    @Basic
    @Column(name = "flight_number")
    private String fltNo;

    @Basic
    @Column(name = "flight_time")
    @Temporal(TemporalType.TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="HH:mm")
    private Date flightTime;

    @Basic
    @Column(name = "origin")
    private String origin;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fare_id")
    private Fare fare;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "flight_info_id")
    private FlightInfo flightInfo;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
    

}
