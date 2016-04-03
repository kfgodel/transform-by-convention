package convention.transformers.datetimes;

import ar.com.kfgodel.nary.api.Nary;
import net.sf.kfgodel.bean2bean.annotations.FormatterPattern;
import net.sf.kfgodel.bean2bean.conversion.converters.ConverterUtils;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

/**
 * This type offers som helper methods to subtypes
 * Created by kfgodel on 03/04/16.
 */
public class LocalDateTimeConverterSupport {
  /**
   * Tries to get the custom format that the user wants to apply
   *
   * @param contextAnnotations The annotations to get it from
   * @param sourceObject
   * @param expectedType       @return The custom format or an empty nary if there's no format
   */
  protected Nary<DateTimeFormatter> getUserFormatterFrom(Annotation[] contextAnnotations, Object sourceObject, Type expectedType) {
    if (contextAnnotations == null || contextAnnotations.length == 0) {
      return Nary.empty();
    }
    final FormatterPattern pattern = ConverterUtils.getContextAnnotationLike(FormatterPattern.class, contextAnnotations);
    if (pattern == null) {
      // No hay annotation de formateo
      return Nary.empty();
    }
    String userFormat = pattern.value();
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(userFormat);
      return Nary.of(formatter);
    } catch (IllegalArgumentException e) {
      throw new CannotConvertException("El formato indicado[" + userFormat + "] no es valido para un DateTimeFormatter", sourceObject, expectedType, e);
    }
  }
}
