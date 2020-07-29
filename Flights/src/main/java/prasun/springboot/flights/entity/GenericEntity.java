package prasun.springboot.flights.entity;

import java.io.Serializable;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class GenericEntity implements IdentifierGenerator{

	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
		String query = String.format("select max(%s) from %s", 
	            session.getEntityPersister(obj.getClass().getName(), obj)
	              .getIdentifierPropertyName(),
	            obj.getClass().getSimpleName());
	 
			Stream ids = session.createQuery(query).stream();
	 
	        Long max = ids.flatMapToLong(Long::longValue)
	          .max()
	          .orElse(0L);
	 
	        return (max + 1);

	}
	
	

}
