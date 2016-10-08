package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.contexts.TestContext;
import convention.transformers.dates.LocalDate2StringConverter;
import convention.transformers.dates.String2LocalDateConverter;
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

  LocalDateTime2StringConverter localDateTime2String();

  void localDateTime2String(Supplier<LocalDateTime2StringConverter> definition);

  String2LocalDateTimeConverter string2localDateTime();

  void string2localDateTime(Supplier<String2LocalDateTimeConverter> definition);

  LocalDate2StringConverter localDate2String();

  void localDate2String(Supplier<LocalDate2StringConverter> definition);

  String2LocalDateConverter string2LocalDate();

  void string2LocalDate(Supplier<String2LocalDateConverter> definition);

  DecimalString2DoubleConverter string2double();
  void string2double(Supplier<DecimalString2DoubleConverter> definition);

  Double2DecimalStringConverter double2string();
  void double2string(Supplier<Double2DecimalStringConverter> definition);

  Double2LongConverter double2long();
  void double2long(Supplier<Double2LongConverter> definition);

}
