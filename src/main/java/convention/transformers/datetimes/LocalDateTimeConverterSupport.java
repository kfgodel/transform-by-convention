package convention.transformers.datetimes;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.transformbyconvention.impl.converters.FormattedConverterSupport;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

/**
 * This type offers som helper methods to subtypes
 * Created by kfgodel on 03/04/16.
 */
public class LocalDateTimeConverterSupport extends FormattedConverterSupport {
  /**
   * Tries to get the custom format that the user wants to apply
   *
   * @param contextAnnotations The annotations to get it from
   * @param sourceObject the object that is going to be converted
   * @param expectedType The expected type
   * @return The custom format or an empty nary if there's no format
   */
  protected Nary<DateTimeFormatter> getUserFormatterFrom(Annotation[] contextAnnotations, Object sourceObject, Type expectedType) {
    Nary<String> foundPattern = getUserFormatterPatterFrom(contextAnnotations);
    return foundPattern
      .mapNary((userFormat) -> this.asDateTimeFormatter(userFormat, sourceObject, expectedType));
  }

  protected DateTimeFormatter asDateTimeFormatter(String userFormat, Object sourceObject, Type expectedType) {
    try {
      return DateTimeFormatter.ofPattern(userFormat);
    } catch (IllegalArgumentException e) {
      throw new CannotConvertException("El formato indicado[" + userFormat + "] no es valido para un DateTimeFormatter", sourceObject, expectedType, e);
    }
  }
}
