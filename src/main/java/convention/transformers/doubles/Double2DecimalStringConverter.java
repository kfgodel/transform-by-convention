/**
 * 27/05/2011 11:52:32 Copyright (C) 2011 10Pines S.R.L.
 */
package convention.transformers.doubles;

import ar.com.kfgodel.nary.api.Nary;
import ar.com.kfgodel.transformbyconvention.impl.converters.FormattedConverterSupport;
import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Locale;

/**
 * Esta clase sabe como convertir de un Double a un string con la coma como separador de decimal
 * 
 * @author D. García
 */
public class Double2DecimalStringConverter extends FormattedConverterSupport implements SpecializedTypeConverter<Double, String> {

	/**
	 * @see SpecializedTypeConverter#convertTo(Type,
	 *      Object, Annotation[])
	 */
	@Override
	public String convertTo(final Type expectedType, final Double sourceObject, final Annotation[] contextAnnotations)
			throws CannotConvertException {
		if (sourceObject == null) {
			// Por las dudas
			return null;
		}

		Nary<String> userFormat = getUserFormatterPatterFrom(contextAnnotations);
		// Si no nos indican uno, agrupamos miles dejamos 2 decimales
		String providedFormat = userFormat.orElse("%,.2f");

		// En alemania usan los mismos simbolos para separar miles y decimales
		// Lo usamos porque viene predefinido, y el nuestro no
		try {
			final String formatted = String.format(Locale.GERMANY, providedFormat, sourceObject);
			return formatted;
		} catch (IllegalArgumentException e) {
			throw new CannotConvertException("El formato de conversion[" + providedFormat + "] es invalido " +
				"y no puede utilizarse con el numero[" + sourceObject + "]", sourceObject, expectedType, e);
		}
	}

	public static Double2DecimalStringConverter create() {
		Double2DecimalStringConverter converter = new Double2DecimalStringConverter();
		return converter;
	}

}
