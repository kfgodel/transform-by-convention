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
import org.joda.time.DateTime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Esta clase sabe como convertir desde un String a un objeto DateTime de Joda
 * 
 * @author D. García
 */
public class String2DateTimeConverter implements SpecializedTypeConverter<String, DateTime> {

	/**
	 * @see SpecializedTypeConverter#convertTo(Type,
	 *      Object, Annotation[])
	 */
	@Override
	public DateTime convertTo(final Type expectedType, final String sourceObject, final Annotation[] contextAnnotations)
			throws CannotConvertException {
		try {
			final DateTime dateTime = new DateTime(sourceObject);
			return dateTime;
		} catch (final IllegalArgumentException e) {
			throw new CannotConvertException("El formato del String es rechazado por DateTime", sourceObject,
					expectedType, e);
		}
	}

	public static String2DateTimeConverter create() {
		String2DateTimeConverter converter = new String2DateTimeConverter();
		return converter;
	}

}
