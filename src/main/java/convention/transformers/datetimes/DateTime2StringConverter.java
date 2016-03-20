/**
 * 06/04/2011 14:00:09 Copyright (C) 2011 10Pines S.R.L.
 */
package convention.transformers.datetimes;

import net.sf.kfgodel.bean2bean.annotations.FormatterPattern;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.converters.ConverterUtils;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Esta clase permite convertir de fechas {@link DateTime} a String en formato ISO
 * 
 * @author D.García
 */
public class DateTime2StringConverter implements SpecializedTypeConverter<DateTime, String> {

	/**
	 * @see SpecializedTypeConverter#convertTo(Type,
	 *      Object, Annotation[])
	 */
	@Override
	public String convertTo(final Type expectedType, final DateTime sourceObject, final Annotation[] contextAnnotations)
			throws CannotConvertException {
		String formatPattern = null;
		if (contextAnnotations != null) {
			final FormatterPattern pattern = ConverterUtils.getContextAnnotationLike(FormatterPattern.class, contextAnnotations);
			if (pattern != null) {
				// El usuario indicó un formato adicional
				formatPattern = pattern.value();
			}
		}
		final String isoDate;

		if (formatPattern == null || formatPattern.isEmpty()) {
			// Si usamos el formato ISO lo expresamos desde el timezone 0.
			final DateTime utcExpressedDatetime = sourceObject.withZone(DateTimeZone.UTC);
			isoDate = utcExpressedDatetime.toString();
		} else {
			isoDate = sourceObject.toString(formatPattern);
		}
		return isoDate;
	}

	public static DateTime2StringConverter create() {
		DateTime2StringConverter converter = new DateTime2StringConverter();
		return converter;
	}

}
