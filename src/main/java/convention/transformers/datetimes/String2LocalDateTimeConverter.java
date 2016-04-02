/**
 * 06/04/2011 13:48:49 Copyright (C) 2006 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package convention.transformers.datetimes;

import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Esta clase sabe como convertir desde un String iso expresado en UTC a un timestamp local
 * 
 * @author D. García
 */
public class String2LocalDateTimeConverter implements SpecializedTypeConverter<String, LocalDateTime> {

	/**
	 * @see SpecializedTypeConverter#convertTo(Type,
	 *      Object, Annotation[])
	 */
	@Override
	public LocalDateTime convertTo(final Type expectedType, final String sourceObject, final Annotation[] contextAnnotations)
			throws CannotConvertException {
		try {
			ZonedDateTime utcTime = ZonedDateTime.parse(sourceObject, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
			final LocalDateTime dateTime = utcTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
			return dateTime;
		} catch (final IllegalArgumentException e) {
			throw new CannotConvertException("El formato del String es rechazado por DateTime", sourceObject,
					expectedType, e);
		}
	}

	public static String2LocalDateTimeConverter create() {
		String2LocalDateTimeConverter converter = new String2LocalDateTimeConverter();
		return converter;
	}

}
