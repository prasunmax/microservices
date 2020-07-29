package prasun.springboot.flights.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@GenericGenerator(name = "idGenerator",strategy = "prasun.springboot.flights.entity.util.GenericIdentifier")
public class GenericEntity {
	
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    @GeneratedValue(generator = "idGenerator")
    @Id
    private Long id;

}
