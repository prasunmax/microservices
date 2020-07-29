package prasun.springboot.flights.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "airline_info")
public class AirlineInfo {

    @Column(name = "airline_id", nullable = false, insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Basic
    @Column(name = "airline_logo")
    private String logo;

    @Basic
    @Column(name = "name_of_airline")
    private String airlineName;

    public AirlineInfo(String logo, String airlineName) {
        super();
        this.logo = logo;
        this.airlineName = airlineName;
    }


}