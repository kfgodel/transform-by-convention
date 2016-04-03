package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.JavaSpec;
import ar.com.dgarcia.javaspec.api.JavaSpecRunner;
import convention.transformers.doubles.Double2LongConverter;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This type verifies the behavior of the double to long converter
 * Created by kfgodel on 03/04/16.
 */
@RunWith(JavaSpecRunner.class)
public class Double2LongConverterTest extends JavaSpec<TransformerTestContext> {
  @Override
  public void define() {
    describe("a double to long converter", () -> {
      context().double2long(Double2LongConverter::create);

      it("returns null if null passed", () -> {
        Long converted = context().double2long().convertTo(null, null, null);
        assertThat(converted).isNull();
      });

      it("converts the given double to its long representation", () -> {
        Long converted = context().double2long().convertTo(null, 1.0, null);
        assertThat(converted).isEqualTo(1L);
      });

      it("rounds the double decimals when converting", () -> {
        Long converted = context().double2long().convertTo(null, 1.5, null);
        assertThat(converted).isEqualTo(2L);
      });

    });

  }
}