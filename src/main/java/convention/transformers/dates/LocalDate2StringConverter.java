/**
 * 06/04/2011 14:00:09 Copyright (C) 2011 10Pines S.R.L.
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
 * Esta clase permite convertir de una fecha local a un string en formato iso
 *
 * @author D.García
 */
public class LocalDate2StringConverter extends DateTimeFormattedConverterSupport implements SpecializedTypeConverter<LocalDate, String> {

  @Override
  public String convertTo(Type expectedType, LocalDate sourceObject, Annotation[] contextAnnotations) throws CannotConvertException {
    if (sourceObject == null) {
      return null;
    }

    Nary<DateTimeFormatter> userprovidedFormatter = getUserFormatterFrom(contextAnnotations, sourceObject, expectedType);
    return userprovidedFormatter.mapOptional((userFormatter) -> {
      // We format as the user requested
      String formatted = sourceObject.format(userFormatter);
      return formatted;
    }).orElseGet(() -> {
      // If no user format we do ISO format for dates
      String formatted = sourceObject.format(DateTimeFormatter.ISO_DATE);
      return formatted;
    });

  }

  public static LocalDate2StringConverter create() {
    LocalDate2StringConverter converter = new LocalDate2StringConverter();
    return converter;
  }


}
