package prasun.springboot.airline.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@MappedSuperclass
@Data
//@GenericGenerator(name = "idGenerator",strategy = "prasun.springboot.flights.entity.util.GenericIdentifier")
@GenericGenerator(name = "idGenerator",strategy = "uuid2")
public class GenericEntity {
	
    @Column(nullable = false, insertable = false, updatable = false)
    @GeneratedValue(generator = "idGenerator")
    @Id
    @JsonProperty("_id")
    private Integer _id;

}
