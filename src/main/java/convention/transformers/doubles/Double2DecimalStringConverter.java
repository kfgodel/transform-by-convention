/**
 * 27/05/2011 11:52:32 Copyright (C) 2011 10Pines S.R.L.
 */
package convention.transformers.doubles;

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
public class Double2DecimalStringConverter implements SpecializedTypeConverter<Double, String> {

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
		// En alemania usan el mismo formato decimal y viene predefinido (a dif de nosotros o
		// españa)
		// El formato indica que queremos agrupar miles, y que son 2 decimales
		final String formatted = String.format(Locale.GERMANY, "%,.2f", sourceObject);
		return formatted;
	}

	public static Double2DecimalStringConverter create() {
		Double2DecimalStringConverter converter = new Double2DecimalStringConverter();
		return converter;
	}

}
