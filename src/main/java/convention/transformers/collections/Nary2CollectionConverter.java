package convention.transformers.collections;

import ar.com.kfgodel.nary.api.Nary;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
import net.sf.kfgodel.bean2bean.conversion.converters.Collection2CollectionConverter;
import net.sf.kfgodel.bean2bean.conversion.converters.CouldNotInstanstiateException;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import net.sf.kfgodel.dgarcia.lang.reflection.ReflectionUtils;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

/**
 * This type allows conversion from nary instances to collections
 * Created by kfgodel on 05/04/15.
 */
public class Nary2CollectionConverter implements SpecializedTypeConverter<Nary, Collection> {

    @Inject
    private TypeConverter converter;


    @Override
    public Collection convertTo(Type expectedType, Nary sourceObject, Annotation[] contextAnnotations) throws CannotConvertException {
        if (expectedType == null) {
            throw new CannotConvertException("Cannot make conversion. Expected type was not defined", sourceObject,
                    expectedType);
        }
        final Class<?> expectedClass = ReflectionUtils.degenerify(expectedType);
        Collection destinationCol;
        try {
            int initialSize = 10;
            destinationCol = Collection2CollectionConverter.instantiate(expectedClass, initialSize);
        } catch (final CouldNotInstanstiateException e) {
            throw new CannotConvertException("Couldn't instantiate a collection", sourceObject, expectedType, e);
        }

        final Type elementType = ReflectionUtils.getElementTypeParameterFrom(expectedType);
        Iterator iterator = sourceObject.iterator();
        while(iterator.hasNext()){
            Object sourceElement = iterator.next();
            Object elementToAdd = sourceElement;
            if (elementType != null) {
                elementToAdd = this.converter.convertValue(sourceElement, elementType);
            }
            destinationCol.add(elementToAdd);
        }
        return destinationCol;
    }

    public static Nary2CollectionConverter create(TypeConverter converter) {
        Nary2CollectionConverter collectionConverter = new Nary2CollectionConverter();
        collectionConverter.converter = converter;
        return collectionConverter;
    }

}
