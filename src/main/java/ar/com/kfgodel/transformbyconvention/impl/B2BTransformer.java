package ar.com.kfgodel.transformbyconvention.impl;

import ar.com.kfgodel.convention.api.Convention;
import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.kfgodel.transformbyconvention.api.config.TransformerConfiguration;
import ar.com.kfgodel.transformbyconvention.api.exceptions.TransformerException;
import com.google.common.collect.Lists;
import net.sf.kfgodel.bean2bean.Bean2Bean;
import net.sf.kfgodel.bean2bean.conversion.DefaultTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
import net.sf.kfgodel.bean2bean.conversion.converters.*;
import net.sf.kfgodel.bean2bean.instantiation.EmptyConstructorObjectFactory;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This type implements the converter using bean2bean
 * Created by kfgodel on 05/04/15.
 */
public class B2BTransformer implements TypeTransformer {
  public static Logger LOG = LoggerFactory.getLogger(B2BTransformer.class);

  private Bean2Bean b2bManipulator;
  private TypeConverter b2bConverter;
  private TransformerConfiguration config;

  public static B2BTransformer create(TransformerConfiguration config) {
    B2BTransformer converter = new B2BTransformer();
    converter.config = config;
    converter.initialize();
    return converter;
  }

  private void initialize() {
    b2bConverter = DefaultTypeConverter.create();
    b2bManipulator = Bean2Bean.create(b2bConverter);

    // Make dependencies available for injection into converters
    config.getInjector().bindTo(TypeConverter.class, b2bConverter);
    config.getInjector().bindTo(Bean2Bean.class, b2bManipulator);

    // Use niladic constructor to instantiate types
    b2bConverter.setObjectFactory(EmptyConstructorObjectFactory.create());

    // Converters for basic conversions
    registerPredefinedConverters();

    // Custom extension converters available on the classpath
    registerClasspathConverters();
  }

  /**
   * Adds converters that can be discovered on the classpath based on conventions
   */
  private void registerClasspathConverters() {
    discoverTransformerClasses().
      forEach(this::registerDiscoveredTransformer);
  }

  private void registerDiscoveredTransformer(Class<? extends SpecializedTypeConverter> transformerClass) {
    SpecializedTypeConverter converter = config.getInjector().createInjected(transformerClass);

    List<Type> converterParameters = getConverterScopeFromSupertypeDeclaration(transformerClass);
    Class sourceClass = (Class) converterParameters.get(0);
    Type destinationType = converterParameters.get(1);

    b2bConverter.registerSpecializedConverterFor(sourceClass, destinationType, converter);
  }

  private List<Type> getConverterScopeFromSupertypeDeclaration(Class<? extends SpecializedTypeConverter> transformerClass) {
    Optional<ParameterizedType> typeConverterDeclaration = Arrays.stream(transformerClass.getGenericInterfaces())
      .filter(ParameterizedType.class::isInstance)
      .map(ParameterizedType.class::cast)
      .filter((parameterizedType -> SpecializedTypeConverter.class.equals(parameterizedType.getRawType())))
      .findFirst();
    return typeConverterDeclaration.map((superType)->{
      Type[] actualTypeArguments = superType.getActualTypeArguments();
      Type sourceType = actualTypeArguments[0];
      if(!Class.class.isInstance(sourceType)){
        throw new TransformerException("First argument ["+sourceType+"] on " + SpecializedTypeConverter.class + "  must be a class for " + transformerClass);
      }
      Type destinationType = actualTypeArguments[1];
      return Lists.newArrayList(sourceType, destinationType);
    }).orElseThrow(() -> {
      return new TransformerException("Transformer class[" + transformerClass + "] must implement " + SpecializedTypeConverter.class + " to be auto-registered");
    });
  }

  /**
   * Explores all the config packages to discover transformer classes to be added for conversion.<br>
   * @return The set of classes extending {@link net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter}
   */
  private Set<Class<? extends SpecializedTypeConverter>> discoverTransformerClasses() {
    Nary<String> transformerPackages = config.getTransformerPackages();
    Set<Class<? extends SpecializedTypeConverter>> transformerClasses = transformerPackages
      .flatMap(this::getTransformerClassesIn)
      .collect(Collectors.toSet());

    if (transformerClasses.isEmpty()) {
      LOG.info("No converters found in[" + Convention.create().getConverterPackageNames() + "]");
    }else{
      LOG.info("Found {} converter types: {}", transformerClasses.size(), transformerClasses);
    }

    return transformerClasses;
  }

  /**
   * Explores the types inside the given package to discover subtypes of {@link net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter}
   * @param resourcePackage The root package to explore
   * @return The stream of types inside the package or subpackages
   */
  private Stream<? extends Class<? extends SpecializedTypeConverter>> getTransformerClassesIn(String resourcePackage) {
    Reflections reflections = new Reflections(resourcePackage);
    Set<Class<? extends SpecializedTypeConverter>> transformerTypes = reflections.getSubTypesOf(SpecializedTypeConverter.class);
    return transformerTypes.stream();
  }

  /**
   * Adds to bean2bean the converters that are neede for basic convertions
   */
  private void registerPredefinedConverters() {
    // Register specialized converters (order is irrelevant)

    // Enums
    b2bConverter.registerSpecializedConverterFor(String.class, Enum.class, String2EnumConverter.create());
    b2bConverter.registerSpecializedConverterFor(Enum.class, String.class, Enum2StringConverter.create());

    // Numbers
    b2bConverter.registerSpecializedConverterFor(String.class, Number.class, String2NumberConverter.create());

    //Collections
    b2bConverter.registerSpecializedConverterFor(Collection.class, Collection.class, Collection2CollectionConverter.create(b2bConverter));


    // Register general converters (order indicates precedence)

    // TO converter
    b2bConverter.registerGeneralConverter(AnnotatedClassConverter.create(b2bManipulator));

    // Arrays
    b2bConverter.registerGeneralConverter(ArrayCollectionConverter.create(b2bConverter));
    b2bConverter.registerGeneralConverter(ArrayArrayConverter.create(b2bConverter));

    // Types that don't need conversion
    b2bConverter.registerGeneralConverter(PolymorphismConverter.create());

    // General String converter using json
    b2bConverter.registerGeneralConverter(JsonStringObjectConverter.create());

    // Primitive type converter
    b2bConverter.registerGeneralConverter(WrappedXWorkConverter.create());

    // Optional converter used by name
    b2bConverter.registerGeneralConverter(FormatterConverter.create());
  }


  @Override
  public <T> T transformTo(Class<T> expectedClass, Object sourceObject) {
    return b2bConverter.convertValueToClass(expectedClass, sourceObject);
  }

  @Override
  public <R> R transformTo(Type expectedType, Object sourceObject) {
    return b2bConverter.convertValue(sourceObject, expectedType);
  }
}
