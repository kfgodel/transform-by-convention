package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import convention.transformers.dates.String2LocalDateConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This type verifies the behavior of the string to local date converter
 * Created by kfgodel on 03/04/16.
 */
@RunWith(JavaSpecRunner.class)
public class String2LocalDateConverterTest extends JavaSpec<TransformerTestContext> {
  @Override
  public void define() {
    describe("a string to LocalDate converter", () -> {
      context().string2LocalDate(String2LocalDateConverter::create);

      it("returns null if null passed as string", () -> {
        LocalDate localDate = context().string2LocalDate().convertTo(null, null, null);
        assertThat(localDate).isNull();
      });

      it("converts a string expressed in ISO format to a local date", () -> {
        String isoDate = "2016-04-03";
        LocalDate converted = context().string2LocalDate().convertTo(null, isoDate, null);
        assertThat(converted).isEqualTo(LocalDate.of(2016, 04, 03));
      });

      it("parses with a different format when passed as additional annotations", () -> {
        String isoDate = "03/04/2016";
        LocalDate converted = context().string2LocalDate().convertTo(null, isoDate, LocalDate2StringConverterTest.FakeAnnotatedClass.getAttributeAnnotations());
        assertThat(converted).isEqualTo(LocalDate.of(2016, 04, 03));
      });

      it("throws an exception if the user format is invalid", () -> {
        String isoDate = "03/04/2016";
        try {
          context().string2LocalDate().convertTo(null, isoDate, LocalDate2StringConverterTest.FakeAnnotatedClass.getInvalidAnnotations());
          failBecauseExceptionWasNotThrown(CannotConvertException.class);
        } catch (CannotConvertException e) {
          assertThat(e).hasMessage("El formato indicado[le invalid format] no es valido para un DateTimeFormatter");
        }
      });

    });

  }
}