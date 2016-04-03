package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.fields.TypeField;
import ar.com.kfgodel.diamond.api.types.TypeInstance;
import convention.transformers.doubles.Double2DecimalStringConverter;
import net.sf.kfgodel.bean2bean.annotations.FormatterPattern;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * Created by kfgodel on 03/04/16.
 */
@RunWith(JavaSpecRunner.class)
public class Double2DecimalStringConverterTest extends JavaSpec<TransformerTestContext> {
  @Override
  public void define() {
    describe("a double to decimal string converter", () -> {
      context().double2string(Double2DecimalStringConverter::create);

      it("returns null if null passed", () -> {
        String converted = context().double2string().convertTo(null, null, null);
        assertThat(converted).isNull();
      });

      it("uses the comma as the decimal separator symbol", () -> {
        String converted = context().double2string().convertTo(null, 1.0, null);
        assertThat(converted).isEqualTo("1,00");
      });

      it("uses the point as thousands group separator symbol", () -> {
        String converted = context().double2string().convertTo(null, 1000.0, null);
        assertThat(converted).isEqualTo("1.000,00");
      });

      it("formats with the user format if one is provided as annotation", () -> {
        String converted = context().double2string().convertTo(null, 1.0, FakeAnnotatedClass.getAttributeAnnotations());
        assertThat(converted).isEqualTo("0001,000");
      });

      it("throws an exception if the format is invalid", () -> {
        try {
          context().double2string().convertTo(null, 1.0, FakeAnnotatedClass.getInvalidAnnotations());
          failBecauseExceptionWasNotThrown(CannotConvertException.class);
        } catch (CannotConvertException e) {
          assertThat(e).hasMessage("El formato de conversion[%2] es invalido y no puede utilizarse con el numero[1.0]");
        }
      });
    });

  }

  /**
   * Test class created to have a {@link FormatterPattern} annotation
   */
  public static class FakeAnnotatedClass {

    @FormatterPattern("%08.3f")
    private String attribute;

    @FormatterPattern("%2")
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