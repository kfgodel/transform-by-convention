package ar.com.kfgodel.transformbyconvention.impl;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.transformbyconvention.api.TypeTransformer;
import ar.com.kfgodel.transformbyconvention.api.tos.EnumTo;
import ar.com.kfgodel.transformbyconvention.api.tos.PersistibleTo;
import ar.com.tenpines.orm.api.HibernateOrm;
import ar.com.tenpines.orm.api.entities.Identifiable;
import convention.persistent.PersistentSupport;
import convention.transformers.collections.Nary2CollectionConverter;
import convention.transformers.datetimes.DateTime2StringConverter;
import convention.transformers.datetimes.String2DateTimeConverter;
import convention.transformers.doubles.DecimalString2DoubleConverter;
import convention.transformers.doubles.Double2DecimalStringConverter;
import convention.transformers.doubles.Double2LongConverter;
import convention.transformers.enums.EnumTo2EnumConverter;
import convention.transformers.persistibles.Identifiable2NumberConverter;
import convention.transformers.persistibles.Number2PersistentConverter;
import convention.transformers.persistibles.PersistibleTo2PersistentConverter;
import net.sf.kfgodel.bean2bean.Bean2Bean;
import net.sf.kfgodel.bean2bean.conversion.DefaultTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
import net.sf.kfgodel.bean2bean.conversion.converters.*;
import net.sf.kfgodel.bean2bean.instantiation.EmptyConstructorObjectFactory;
import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * This type implements the converter using bean2bean
 * Created by kfgodel on 05/04/15.
 */
public class B2BTransformer implements TypeTransformer {

    private Bean2Bean b2bManipulator;
    private TypeConverter b2bConverter;

    public static B2BTransformer create(HibernateOrm persistenceModule) {
        B2BTransformer converter = new B2BTransformer();
        converter.initialize(persistenceModule);
        return converter;
    }

    private void initialize(HibernateOrm persistenceModule) {
        b2bConverter = DefaultTypeConverter.create();
        b2bManipulator = Bean2Bean.create(b2bConverter);

        // Use niladic constructor to instantiate types
        b2bConverter.setObjectFactory(EmptyConstructorObjectFactory.create());

        // Register specialized converters (order is irrelevant)

        // Datetimes
        b2bConverter.registerSpecializedConverterFor(String.class, DateTime.class, String2DateTimeConverter.create());
        b2bConverter.registerSpecializedConverterFor(DateTime.class, String.class, DateTime2StringConverter.create());

        // Enums
        b2bConverter.registerSpecializedConverterFor(String.class, Enum.class, String2EnumConverter.create());
        b2bConverter.registerSpecializedConverterFor(Enum.class, String.class, Enum2StringConverter.create());
        b2bConverter.registerSpecializedConverterFor(EnumTo.class, Enum.class, EnumTo2EnumConverter.create());

        // Doubles
        b2bConverter.registerSpecializedConverterFor(String.class, Double.class, DecimalString2DoubleConverter.create());
        b2bConverter.registerSpecializedConverterFor(Double.class, String.class, Double2DecimalStringConverter.create());
        b2bConverter.registerSpecializedConverterFor(Double.class, Long.class, Double2LongConverter.create());

        // Numbers
        b2bConverter.registerSpecializedConverterFor(String.class, Number.class, String2NumberConverter.create());

        //Collections
        b2bConverter.registerSpecializedConverterFor(Collection.class, Collection.class, Collection2CollectionConverter.create(b2bConverter));
        b2bConverter.registerSpecializedConverterFor(Nary.class, Collection.class, Nary2CollectionConverter.create(b2bConverter));

        // Persistent objects
        b2bConverter.registerSpecializedConverterFor(Identifiable.class, Long.class, Identifiable2NumberConverter.create());
        b2bConverter.registerSpecializedConverterFor(Number.class, PersistentSupport.class, Number2PersistentConverter.create(persistenceModule));
        b2bConverter.registerSpecializedConverterFor(PersistibleTo.class, PersistentSupport.class, PersistibleTo2PersistentConverter.create(b2bManipulator, persistenceModule));


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
