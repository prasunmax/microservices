package prasun.springboot.airline.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "fare")
public class Fare extends GenericEntity{
	
    @Basic
    @Column(name = "currency")
    private String currency;

    @Basic
    @Column(name = "fare", nullable = false)
    private Double fare;

    
}
