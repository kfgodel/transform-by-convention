package ar.com.kfgodel.transformbyconvention.api;

import java.lang.reflect.Type;

/**
 * This type represents a general type converter used by the application to transform objects
 * from one type to other
 * Created by kfgodel on 05/04/15.
 */
public interface TypeTransformer {

    /**
     * Transforms the given source object to the expected type indicated by a class
     * @param expectedClass The class of expected type
     * @param sourceObject The object to transform
     * @param <T> The expected destination type
     * @return The transformed object
     */
    <T> T transformTo(Class<T> expectedClass, Object sourceObject);


    /**
     * Transforms the given source object to the expected type indicated by a class
     * @param expectedType The instance that indicates expected type
     * @param sourceObject The object to transform
     * @param <R> The expected destination type
     * @return The transformed object
     */
    <R> R transformTo(Type expectedType, Object sourceObject);
}
