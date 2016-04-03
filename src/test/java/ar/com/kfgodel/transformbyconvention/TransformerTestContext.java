package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.TestContext;
import convention.transformers.datetimes.LocalDateTime2StringConverter;
import convention.transformers.datetimes.String2LocalDateTimeConverter;
import convention.transformers.doubles.DecimalString2DoubleConverter;
import convention.transformers.doubles.Double2DecimalStringConverter;
import convention.transformers.doubles.Double2LongConverter;

import java.util.function.Supplier;

/**
 * This type represents the test context of all the transformer tests
 * Created by kfgodel on 03/04/16.
 */
public interface TransformerTestContext extends TestContext {

  LocalDateTime2StringConverter local2string();
  void local2string(Supplier<LocalDateTime2StringConverter> definition);

  String2LocalDateTimeConverter string2local();
  void string2local(Supplier<String2LocalDateTimeConverter> definition);

  DecimalString2DoubleConverter string2double();
  void string2double(Supplier<DecimalString2DoubleConverter> definition);

  Double2DecimalStringConverter double2string();
  void double2string(Supplier<Double2DecimalStringConverter> definition);

  Double2LongConverter double2long();

  void double2long(Supplier<Double2LongConverter> definition);

}
