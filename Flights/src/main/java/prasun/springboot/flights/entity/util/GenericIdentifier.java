package prasun.springboot.flights.entity.util;

import java.io.Serializable;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class GenericIdentifier implements IdentifierGenerator {


    @SuppressWarnings("unchecked")
	@Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {

        String query = String.format("select max(%s) from %s", 
            session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName(),
            obj.getClass().getSimpleName());

        Stream<Long> id = session.createQuery(query).stream();
        Long max = id.findFirst().get();
		/*
		 * Long max = ids.map(o -> o.replace(prefix + "-", ""))
		 * .mapToLong(Long::parseLong) .max() .orElse(0L);
		 */

        return (max + 1);
    }

    
//    @Override
//    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
//        prefix = properties.getProperty("prefix");
//    }

}
