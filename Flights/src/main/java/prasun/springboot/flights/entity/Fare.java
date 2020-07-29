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
@Table(name = "fare")
public class Fare {

    @Column(name = "fare_id", nullable = false, insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Basic
    @Column(name = "currency")
    private String currency;

    @Basic
    @Column(name = "fare", nullable = false)
    private Double fare;

	public Fare(String currency, Double fare) {
		super();
		this.currency = currency;
		this.fare = fare;
	}
    
}
