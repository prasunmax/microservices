package prasun.springboot.flights.entity.util;

import java.io.Serializable;
import java.util.stream.Stream;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericIdentifier implements IdentifierGenerator {

	private static final Logger log = LoggerFactory.getLogger(GenericIdentifier.class);

	@SuppressWarnings("unchecked")
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {

		String query = String.format("select max(%s) from %s",
				session.getEntityPersister(obj.getClass().getName(), obj).getIdentifierPropertyName(),
				obj.getClass().getSimpleName());
		log.info("Table Query" + query);
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
