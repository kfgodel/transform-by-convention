/**
 * 06/04/2011 14:00:09 Copyright (C) 2011 10Pines S.R.L.
 */
package convention.transformers.datetimes;

import net.sf.kfgodel.bean2bean.annotations.FormatterPattern;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.converters.ConverterUtils;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Esta clase permite convertir de un timestamp local a un string en formato iso expresado en UTC
 * 
 * @author D.García
 */
public class LocalDateTime2StringConverter implements SpecializedTypeConverter<LocalDateTime, String> {

	/**
	 * @see SpecializedTypeConverter#convertTo(Type,
	 *      Object, Annotation[])
	 */
	@Override
	public String convertTo(final Type expectedType, final LocalDateTime sourceObject, final Annotation[] contextAnnotations)
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
			ZonedDateTime utcTime = sourceObject.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
			isoDate = utcTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		} else {
			isoDate = sourceObject.format(DateTimeFormatter.ofPattern(formatPattern));
		}
		return isoDate;
	}

	public static LocalDateTime2StringConverter create() {
		LocalDateTime2StringConverter converter = new LocalDateTime2StringConverter();
		return converter;
	}

}
