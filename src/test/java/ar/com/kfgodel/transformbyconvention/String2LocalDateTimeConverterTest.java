package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import convention.transformers.datetimes.String2LocalDateTimeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This type verifies the behavior of teh string to local datetime converter
 * Created by kfgodel on 03/04/16.
 */
@RunWith(JavaSpecRunner.class)
public class String2LocalDateTimeConverterTest extends JavaSpec<TransformerTestContext> {
  @Override
  public void define() {
    describe("a string to LocalDateTime converter", () -> {
      context().string2localDateTime(String2LocalDateTimeConverter::create);

      it("returns null if null passed as string", () -> {
        LocalDateTime localDateTime = context().string2localDateTime().convertTo(null, null, null);
        assertThat(localDateTime).isNull();
      });


      describe("with argentina as environment", () -> {
        it("has argentina as the current timezone", () -> {
          // Asumimos que se corre en argentina porque nos permite predecir el valor final de los strings
          // Este test esta para verificar esa asunción
          assertThat(ZoneId.systemDefault()).isEqualTo(ZoneId.of("America/Argentina/Buenos_Aires"));
        });

        it("converts a string expressed in ISO format to a local shifted datetime", () -> {
          String isoDate = "2016-04-03T15:33:00Z";
          LocalDateTime converted = context().string2localDateTime().convertTo(null, isoDate, null);
          assertThat(converted).isEqualTo(LocalDateTime.of(2016, 04, 03, 12, 33, 00));
        });

        it("parses with a different format when passed as additional annotations", () -> {
          String isoDate = "03/04/2016 12:33:00";
          LocalDateTime converted = context().string2localDateTime().convertTo(null, isoDate, LocalDateTime2StringConverterTest.FakeAnnotatedClass.getAttributeAnnotations());
          assertThat(converted).isEqualTo(LocalDateTime.of(2016, 04, 03, 12, 33, 00));
        });

        it("throws an exception if the user format is invalid", () -> {
          String isoDate = "03/04/2016 12:33:00";
          try {
            context().string2localDateTime().convertTo(null, isoDate, LocalDateTime2StringConverterTest.FakeAnnotatedClass.getInvalidAnnotations());
            failBecauseExceptionWasNotThrown(CannotConvertException.class);
          } catch (CannotConvertException e) {
            assertThat(e).hasMessage("El formato indicado[le invalid format] no es valido para un DateTimeFormatter");
          }
        });

      });

    });

  }
}