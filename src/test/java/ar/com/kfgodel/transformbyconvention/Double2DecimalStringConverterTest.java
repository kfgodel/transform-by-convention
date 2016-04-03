package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import convention.transformers.doubles.Double2DecimalStringConverter;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

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

    });

  }
}