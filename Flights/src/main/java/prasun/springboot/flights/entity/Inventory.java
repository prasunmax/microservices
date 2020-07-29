package prasun.springboot.flights.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@GenericGenerator(name = "myGen",strategy = "prasun.springboot.flights.entity.GenericEntity")
@Entity
@Table(name = "inventory")
public class Inventory {

    @Column(name = "inv_id", nullable = false, insertable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Basic
    @Column(name = "count")
    private int count;


	public Inventory(int count) {
		super();
		this.count = count;
	}
    
}
