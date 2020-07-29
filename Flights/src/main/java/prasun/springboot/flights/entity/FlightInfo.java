package prasun.springboot.flights.entity;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Table(name = "flight_info", uniqueConstraints = {
        @UniqueConstraint(columnNames = "flight_number", name = "uniqueNameConstraint")})
public class FlightInfo {

    @Column(name = "flight_infoid", nullable = false, insertable = false, updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "flights_info", joinColumns =
            {@JoinColumn(referencedColumnName = "flight_infoid")}, inverseJoinColumns = {
            @JoinColumn(name = "airline_id")})
    private AirlineInfo airlineInfo;

    public FlightInfo(String fltNo, String fltTyp, Integer noOfSeats) {
        super();
        this.fltNo = fltNo;
        this.fltTyp = fltTyp;
        this.noOfSeats = noOfSeats;
    }

}
