package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.fields.TypeField;
import ar.com.kfgodel.diamond.api.types.TypeInstance;
import convention.transformers.dates.LocalDate2StringConverter;
import net.sf.kfgodel.bean2bean.annotations.FormatterPattern;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This type verifies the behavior of a localdate to string converter
 * Created by kfgodel on 10/08/16.
 */
@RunWith(JavaSpecRunner.class)
public class LocalDate2StringConverterTest extends JavaSpec<TransformerTestContext> {
  @Override
  public void define() {
    describe("a LocalDate to String converter", () -> {
      context().localDate2String(LocalDate2StringConverter::create);

      it("returns null when null passed", () -> {
        String converted = context().localDate2String().convertTo(String.class, null, null);
        assertThat(converted).isNull();
      });

      it("converts a local date to a string in iso format", () -> {
        LocalDate datetime = LocalDate.of(2016, 8, 10);
        String converted = context().localDate2String().convertTo(null, datetime, null);
        assertThat(converted).isEqualTo("2016-08-10");
      });

      it("uses a different format when passed as additional annotations", () -> {
        LocalDate datetime = LocalDate.of(2016, 8, 10);
        String converted = context().localDate2String().convertTo(null, datetime, FakeAnnotatedClass.getAttributeAnnotations());
        assertThat(converted).isEqualTo("10/08/2016");
      });

      it("throws an exception if the user format is invalid", () -> {
        LocalDate datetime = LocalDate.of(2016, 8, 10);
        try {
          context().localDate2String().convertTo(null, datetime, FakeAnnotatedClass.getInvalidAnnotations());
          failBecauseExceptionWasNotThrown(CannotConvertException.class);
        } catch (CannotConvertException e) {
          assertThat(e).hasMessage("El formato indicado[le invalid format] no es valido para un DateTimeFormatter");
        }
      });
    });
  }

  /**
   * Test class created to have a {@link FormatterPattern} annotation
   */
  public static class FakeAnnotatedClass {

    @FormatterPattern("dd/MM/yyyy")
    private String attribute;

    @FormatterPattern("le invalid format")
    private String invalidAnnotation;

    public static Annotation[] getAttributeAnnotations() {
      return getFormatAnnotationsFor("attribute");
    }

    public static Annotation[] getInvalidAnnotations() {
      return getFormatAnnotationsFor("invalidAnnotation");
    }

    private static Annotation[] getFormatAnnotationsFor(String attributeName) {
      TypeInstance metaClass = Diamond.of(FakeAnnotatedClass.class);
      TypeField attribute = metaClass.fields().named(attributeName).get();
      Annotation[] annotations = attribute.annotations()
        .toArray(Annotation[]::new);
      return annotations;
    }
  }
}