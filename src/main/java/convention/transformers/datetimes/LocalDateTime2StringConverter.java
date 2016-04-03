/**
 * 06/04/2011 14:00:09 Copyright (C) 2011 10Pines S.R.L.
 */
package convention.transformers.datetimes;

import ar.com.kfgodel.nary.api.Nary;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
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
public class LocalDateTime2StringConverter extends LocalDateTimeConverterSupport implements SpecializedTypeConverter<LocalDateTime, String> {

  /**
   * @see SpecializedTypeConverter#convertTo(Type, Object, Annotation[])
   */
  @Override
  public String convertTo(final Type expectedType, final LocalDateTime sourceObject, final Annotation[] contextAnnotations)
    throws CannotConvertException {
    if (sourceObject == null) {
      return null;
    }

    Nary<DateTimeFormatter> userprovidedFormatter = getUserFormatterFrom(contextAnnotations, sourceObject, expectedType);
    return userprovidedFormatter.mapOptional((userFormatter) -> {
      // We format as the user requested
      String formatted = sourceObject.format(userFormatter);
      return formatted;
    }).orElseGet(() -> {
      // If no user format we do UTC shift and ISO format
      String formatted = expressAsUtcIso(sourceObject);
      return formatted;
    });
  }

  /**
   * Changes the timezone from the local to UTC (this is needed when other systems can't shift the timezone, i.e: flex)
   * and uses ISO format to express the time
   *
   * @param sourceObject The localdate
   * @return The ISO string version of the UTC referenced instant
   */
  private String expressAsUtcIso(LocalDateTime sourceObject) {
    // Si usamos el formato ISO lo expresamos desde el timezone 0.
    ZonedDateTime locallyZoned = sourceObject.atZone(ZoneId.systemDefault());
    ZonedDateTime utcZoned = locallyZoned.withZoneSameInstant(ZoneId.of("Z"));
    String isoFormatted = utcZoned.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    return isoFormatted;
  }

  public static LocalDateTime2StringConverter create() {
    LocalDateTime2StringConverter converter = new LocalDateTime2StringConverter();
    return converter;
  }

}
