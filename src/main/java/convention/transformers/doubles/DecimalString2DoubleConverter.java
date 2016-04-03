/**
 * 27/05/2011 12:11:20 Copyright (C) 2011 10Pines S.R.L.
 */
package convention.transformers.doubles;

import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Esta clase convierte de String con formato decimal a Double
 * 
 * @author D. García
 */
public class DecimalString2DoubleConverter implements SpecializedTypeConverter<String, Double> {

	private final static Pattern numerosConComa = Pattern.compile("^[-+]?[0-9]*\\,?[0-9]+$");
	private final static Pattern numerosConPuntoYComa = Pattern.compile("^[-+]?[0-9]{1,3}(\\.[0-9]{3})*\\,?[0-9]+$");
	
	/**
	 * Alemania tiene el mismo formato numerico que nosotros, y ya viene predefinido (a diferencia
	 * de AR, o ES)
	 */
	private final NumberFormat numberFormatter = NumberFormat.getInstance(Locale.GERMANY);

	/**
	 * @see SpecializedTypeConverter#convertTo(Type,
	 *      Object, Annotation[])
	 */
	@Override
	public Double convertTo(final Type expectedType, final String sourceObject, final Annotation[] contextAnnotations)
			throws CannotConvertException {
		if (sourceObject == null || sourceObject.trim().isEmpty()) {
			return null;
		}
		
		this.verificarFormato(sourceObject, expectedType);
		
		Number parsed;
		try {
			parsed = numberFormatter.parse(sourceObject);
		} catch (final ParseException e) {
			throw new CannotConvertException("Error desconocido al parsear un texto a numero decimal: \""
					+ sourceObject + "\"", sourceObject, expectedType, e);
		}
		final Double result = parsed.doubleValue();
		return result;
	}

	
	/**
	 * La idea es verificar que la cadena que le llega tenga un formato coherente.
	 * Lo hacemos a mano porque nos dimos cuenta que el numberFormatter no lo hacia.
	 * @param toVerify
	 * @param expectedType
	 */
	protected void verificarFormato(String toVerify, Type expectedType) {
		if (!numerosConComa.matcher(toVerify).matches() && !numerosConPuntoYComa.matcher(toVerify).matches()){
			throw new CannotConvertException("La cadena[" + toVerify + "] no posee el formato esperado. Se esperaba un numero decimal con comas con el formato ###.###,##", toVerify, expectedType);
		}
		
	}

	public static DecimalString2DoubleConverter create() {
		DecimalString2DoubleConverter converter = new DecimalString2DoubleConverter();
		return converter;
	}

}
