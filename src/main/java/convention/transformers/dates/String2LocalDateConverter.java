/**
 * 06/04/2011 13:48:49 Copyright (C) 2006 Darío L. García
 * <p>
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package convention.transformers.dates;

import ar.com.kfgodel.nary.api.Nary;
import convention.transformers.datetimes.DateTimeFormattedConverterSupport;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Esta clase sabe como convertir desde un String iso expresado a una fecha local
 *
 * @author D. García
 */
public class String2LocalDateConverter extends DateTimeFormattedConverterSupport implements SpecializedTypeConverter<String, LocalDate> {

  /**
   * @see SpecializedTypeConverter#convertTo(Type, Object, Annotation[])
   */
  @Override
  public LocalDate convertTo(final Type expectedType, final String sourceObject, final Annotation[] contextAnnotations)
    throws CannotConvertException {
    if (sourceObject == null) {
      return null;
    }

    Nary<DateTimeFormatter> userProvidedFormatter = getUserFormatterFrom(contextAnnotations, sourceObject, expectedType);
    return userProvidedFormatter.mapOptional((userFormatter) -> {
      LocalDate parsed = LocalDate.parse(sourceObject, userFormatter);
      return parsed;
    }).orElseGet(() -> {
      LocalDate parsed = LocalDate.parse(sourceObject, DateTimeFormatter.ISO_DATE);
      return parsed;
    });
  }

  public static String2LocalDateConverter create() {
    String2LocalDateConverter converter = new String2LocalDateConverter();
    return converter;
  }

}
