/**
 * 05/05/2013 17:36:54 Copyright (C) 2011 Darío L. García
 *
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package convention.transformers.enums;

import ar.com.kfgodel.transformbyconvention.api.tos.EnumTo;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Esta clase define el converter que permite convertir del enumTo al enum
 *
 * @author D. García
 */
public class EnumTo2EnumConverter implements SpecializedTypeConverter<EnumTo, Enum> {
    private static final Logger LOG = LoggerFactory.getLogger(EnumTo2EnumConverter.class);

    /**
     * @see SpecializedTypeConverter#convertTo(Type,
     *      Object, Annotation[])
     */
    @Override
    public Enum<?> convertTo(final Type expectedType, final EnumTo sourceObject, final Annotation[] contextAnnotations)
            throws CannotConvertException {
        final String enumName = sourceObject.getId();
        if (enumName == null) {
            LOG.warn("Se intentó convertir un eunmTo con un ID null en un un enum");
            return null;
        }
        if (!(expectedType instanceof Class)) {
            throw new RuntimeException("El tipo de enum[" + expectedType + "] no es una instancia de clase?");
        }
        @SuppressWarnings("unchecked")
        final Class<Enum<?>> enumClass = (Class<Enum<?>>) expectedType;
        final Enum<?>[] allEnums = enumClass.getEnumConstants();
        for (final Enum<?> enumValue : allEnums) {
            if (enumValue.name().equals(enumName)) {
                return enumValue;
            }
        }
        throw new CannotConvertException("No se encontró el enum de nombre[" + enumName + "] en los valores del enum["
                + expectedType + "]", sourceObject, expectedType);
    }

    public static EnumTo2EnumConverter create() {
        EnumTo2EnumConverter converter = new EnumTo2EnumConverter();
        return converter;
    }

}