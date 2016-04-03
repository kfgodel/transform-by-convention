package ar.com.kfgodel.transformbyconvention.impl.converters;

import ar.com.kfgodel.nary.api.Nary;
import net.sf.kfgodel.bean2bean.annotations.FormatterPattern;
import net.sf.kfgodel.bean2bean.conversion.converters.ConverterUtils;

import java.lang.annotation.Annotation;

/**
 * This type serves as base class for converters that have format as an aoptional annotation
 * Created by kfgodel on 03/04/16.
 */
public class FormattedConverterSupport {

  /**
   * Tries to get the custom format that the user wants to apply
   *
   * @param contextAnnotations The annotations to get it from
   */
  protected Nary<String> getUserFormatterPatterFrom(Annotation[] contextAnnotations) {
    if (contextAnnotations == null || contextAnnotations.length == 0) {
      return Nary.empty();
    }
    final FormatterPattern pattern = ConverterUtils.getContextAnnotationLike(FormatterPattern.class, contextAnnotations);
    if (pattern == null) {
      // No hay annotation de formateo
      return Nary.empty();
    }
    return Nary.of(pattern.value());
  }
}
