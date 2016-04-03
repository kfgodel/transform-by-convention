package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import ar.com.kfgodel.diamond.api.Diamond;
import ar.com.kfgodel.diamond.api.fields.TypeField;
import ar.com.kfgodel.diamond.api.types.TypeInstance;
import convention.transformers.datetimes.LocalDateTime2StringConverter;
import net.sf.kfgodel.bean2bean.annotations.FormatterPattern;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the behavior of a localdatetime to string converter
 * Created by kfgodel on 03/04/16.
 */
@RunWith(JavaSpecRunner.class)
public class LocalDateTime2StringConverterTest extends JavaSpec<TransformerTestContext> {
  @Override
  public void define() {
    describe("a LocalDateTime to String converter", () -> {
      context().local2string(LocalDateTime2StringConverter::create);

      describe("with argentina as environment", () -> {
        it("has argentina as the current timezone", () -> {
          // Asumimos que se corre en argentina porque nos permite predecir el valor final de los strings
          // Este test esta para verificar esa asunción
          assertThat(ZoneId.systemDefault()).isEqualTo(ZoneId.of("America/Argentina/Buenos_Aires"));
        });

        it("converts a local date to a string in iso format with a UTC timezone reference", () -> {
          // Expressed as local in a GMT-3 zone
          LocalDateTime datetime = LocalDateTime.of(2016, 04, 03, 12, 33, 00);
          String converted = context().local2string().convertTo(String.class, datetime, null);
          assertThat(converted).isEqualTo("2016-04-03T15:33:00Z");
        });

        it("uses a different format when passed as additional annotations", () -> {
          LocalDateTime datetime = LocalDateTime.of(2016, 04, 03, 12, 33, 00);
          String converted = context().local2string().convertTo(String.class, datetime, FakeAnnotatedClass.getAttributeAnnotations());
          assertThat(converted).isEqualTo("03/04/2016 12:33:00");
        });

        it("returns null when null passed", () -> {
          String converted = context().local2string().convertTo(String.class, null, null);
          assertThat(converted).isNull();
        });

      });

    });
  }

  /**
   * Test class created to have a {@link FormatterPattern} annotation
   */
  public static class FakeAnnotatedClass {

    @FormatterPattern("dd/MM/yyyy HH:mm:ss")
    private String attribute;

    public static Annotation[] getAttributeAnnotations() {
      TypeInstance metaClass = Diamond.of(FakeAnnotatedClass.class);
      TypeField attribute = metaClass.fields().named("attribute").get();
      Annotation[] annotations = attribute.annotations()
        .toArray(Annotation[]::new);
      return annotations;
    }

  }
}