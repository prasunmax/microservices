package prasun.springboot.product.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import prasun.springboot.product.dao.ProductDocumentMapper;

public class HelperUtils {
	private static Logger log = LoggerFactory.getLogger(ProductDocumentMapper.class.getName());
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Date parseDate(Object stringDate) {
		if (stringDate == null) {
			return null;
		}
		try {
			if (stringDate instanceof String) {
				return sdf.parse((String) stringDate);
			}
			if (stringDate instanceof Date) {
				return (Date) stringDate;
			}
		} catch (ParseException ex) {
			log.error("Error parsing `{}` string into Date object: {}", stringDate, ex.getMessage());
		}
		return null;
	}

	public static Integer parseInt(Object o) {
		if (o instanceof String) {
			if ("".equals(o)) {
				return 0;
			}
			return Integer.valueOf((String) o);
		}
		return ((Number) o).intValue();
	}

	public static Double parseDouble(Object rating) {
		if (rating instanceof String) {
			if ("".equals(rating)) {
				return (double) 0;
			}
			return Double.parseDouble((String) rating);
		}
		return ((Number) rating).doubleValue();
	}
}
