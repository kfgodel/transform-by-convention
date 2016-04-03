package ar.com.kfgodel.transformbyconvention;

import ar.com.dgarcia.javaspec.api.TestContext;
import convention.transformers.datetimes.LocalDateTime2StringConverter;

import java.util.function.Supplier;

/**
 * This type represents the test context of all the transformer tests
 * Created by kfgodel on 03/04/16.
 */
public interface TransformerTestContext extends TestContext {

  LocalDateTime2StringConverter local2string();

  void local2string(Supplier<LocalDateTime2StringConverter> definition);

}
