package convention.transformers.persistibles;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.tenpines.orm.api.HibernateOrm;
import ar.com.tenpines.orm.api.operations.basic.FindById;
import convention.persistent.PersistentSupport;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * This type knows how to fetch a persisten object given its id
 * Created by kfgodel on 05/04/15.
 */
public class Number2PersistentConverter implements SpecializedTypeConverter<Number, PersistentSupport> {

    private HibernateOrm hibernate;

    @Override
    public PersistentSupport convertTo(Type expectedType, Number sourceObject, Annotation[] contextAnnotations) throws CannotConvertException {
        if (sourceObject == null) {
            return null;
        }

        if (!Class.class.isInstance(expectedType)) {
            throw new CannotConvertException("El tipo indicado como esperado no es una clase", sourceObject, expectedType);
        }

        if(!PersistentSupport.class.isAssignableFrom((Class)expectedType)){
            throw new CannotConvertException("La clase pasada no hereda de " + PersistentSupport.class, sourceObject, expectedType);
        }

        Class<? extends PersistentSupport> expectedClass = (Class<? extends PersistentSupport>) expectedType;
        Nary<? extends PersistentSupport> foundObject = this.hibernate.ensureSessionFor(FindById.create(expectedClass, sourceObject.longValue()));
        // Si no lo encontramos será null
        return foundObject
                .orElse(null);
    }

    public static Number2PersistentConverter create(HibernateOrm hibernate) {
        Number2PersistentConverter converter = new Number2PersistentConverter();
        converter.hibernate = hibernate;
        return converter;
    }

}
