package ar.com.kfgodel.transformbyconvention.api;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * This type represents a general type converter used by the application to transform objects
 * from one type to other
 * Created by kfgodel on 05/04/15.
 */
public interface TypeTransformer {

  /**
   * Transforms the given source object to the expected type indicated by a class
   *
   * @param expectedClass The class of expected type
   * @param sourceObject  The object to transform
   * @param <T>           The expected destination type
   * @return The transformed object
   */
  <T> T transformTo(Class<T> expectedClass, Object sourceObject);


  /**
   * Transforms the given source object to the expected type indicated by a class
   *
   * @param expectedType The instance that indicates expected type
   * @param sourceObject The object to transform
   * @param <R>          The expected destination type
   * @return The transformed object
   */
  <R> R transformTo(Type expectedType, Object sourceObject);

  /**
   * Creates a transformation function that will convert its input to the given
   * output when called
   *
   * @param expectedType The class that indicates the expected type
   * @param <I>          The type of input of the function
   * @param <R>          The expected output type after conversion
   * @return The converter function
   */
  default <I, R> Function<I, R> convertingTo(Class<R> expectedType) {
    Function<I, R> conversionFunction = (input) -> this.transformTo(expectedType, input);
    return conversionFunction;
  }

  /**
   * Creates a transformation function that will convert its input to the given
   * output when called
   *
   * @param expectedType The type that indicates the expected type
   * @param <I>          The type of input of the function
   * @param <R>          The expected output type after conversion
   * @return The converter function
   */
  default <I, R> Function<I, R> convertingTo(Type expectedType) {
    Function<I, R> conversionFunction = (input) -> transformTo(expectedType, input);
    return conversionFunction;
  }

  /**
   * Creates a transformation function that will convert received input to expected type, applyt the given operation,
   * adn subsequently convert generated output to expected output type.<br>
   *   In this manner this transformer allows communication with other systems by adapting I/O to different types
   *   (used by other systems) but expressing the main domain operation with the domain types
   * @param expectedInput The type of expected input for the domain operation
   * @param domainOperation The operation to apply to the converted input. It generates an output in a domain representation
   * @param expectedOutput The type of expected output for the foreign system, (after converting the domain output)
   * @param <I1> Type of input from the foreign system
   * @param <I2> Type of input as represented in the domain
   * @param <O1> Type of output as represented in the domain
   * @param <O2> Type of output for the foreign system
   * @return The adapter function that when applied converts input, applies the operation, and converts its output
   */
  default <I1, I2, O1, O2> Function<I1, O2> adaptingIo(Class<I2> expectedInput, Function<I2, O1> domainOperation, Class<O2> expectedOutput) {
    Function<I1, I2> adaptInput = this.convertingTo(expectedInput);
    Function<O1, O2> adaptOutput = this.convertingTo(expectedOutput);
    Function<I1, O2> process = adaptInput.andThen(domainOperation).andThen(adaptOutput);
    return process;
  }

  /**
   * Creates a transformation function that will convert received input to expected type, applyt the given operation,
   * adn subsequently convert generated output to expected output type.<br>
   *   In this manner this transformer allows communication with other systems by adapting I/O to different types
   *   (used by other systems) but expressing the main domain operation with the domain types
   * @param expectedInput The type of expected input for the domain operation
   * @param domainOperation The operation to apply to the converted input. It generates an output in a domain representation
   * @param expectedOutput The type of expected output for the foreign system, (after converting the domain output)
   * @param <I1> Type of input from the foreign system
   * @param <I2> Type of input as represented in the domain
   * @param <O1> Type of output as represented in the domain
   * @param <O2> Type of output for the foreign system
   * @return The adapter function that when applied converts input, applies the operation, and converts its output
   */
  default <I1, I2, O1, O2> Function<I1, O2> adaptingIo(Class<I2> expectedInput, Function<I2, O1> domainOperation, Type expectedOutput) {
    Function<I1, I2> adaptInput = this.convertingTo(expectedInput);
    Function<O1, O2> adaptOutput = this.convertingTo(expectedOutput);
    Function<I1, O2> process = adaptInput.andThen(domainOperation).andThen(adaptOutput);
    return process;
  }
}
