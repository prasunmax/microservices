package prasun.springboot.flights.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@MappedSuperclass
@Data
@GenericGenerator(name = "idGenerator",strategy = "prasun.springboot.flights.entity.util.GenericIdentifier")
public class GenericEntity {
	
    @Column(nullable = false, insertable = false, updatable = false)
    @GeneratedValue(generator = "idGenerator")
    @Id
    private Integer id;

}
