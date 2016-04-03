package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import convention.transformers.doubles.DecimalString2DoubleConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * This type verifies the behavior of teh string to double converter
 * Created by kfgodel on 03/04/16.
 */
@RunWith(JavaSpecRunner.class)
public class DecimalString2DoubleConverterTest extends JavaSpec<TransformerTestContext> {
  @Override
  public void define() {
    describe("a decimal string to double converter", () -> {
      context().string2double(DecimalString2DoubleConverter::create);

      it("returns null if null passed", () -> {
        Double converted = context().string2double().convertTo(null, null, null);
        assertThat(converted).isNull();
      });

      it("returns null if an empty string is passed", () -> {
        Double converted = context().string2double().convertTo(null, "", null);
        assertThat(converted).isNull();
      });

      it("returns null if a space filled string is passed", () -> {
        Double converted = context().string2double().convertTo(null, "   ", null);
        assertThat(converted).isNull();
      });

      it("converts a string with a comma as decimal symbol into a double", () -> {
        Double converted = context().string2double().convertTo(null, "12,9", null);
        assertThat(converted).isEqualTo(12.9);
      });

      it("uses the point as thousands group separator", () -> {
        Double converted = context().string2double().convertTo(null, "1.000", null);
        assertThat(converted).isEqualTo(1000.0);
      });

      it("throws an exception if the string uses point as decimal separator", () -> {
        try {
          context().string2double().convertTo(null, "12.9", null);
          failBecauseExceptionWasNotThrown(CannotConvertException.class);
        } catch (CannotConvertException e) {
          assertThat(e).hasMessage("La cadena[12.9] no posee el formato esperado. Se esperaba un numero decimal con comas con el formato #.###,##");
        }
      });

    });
  }
}