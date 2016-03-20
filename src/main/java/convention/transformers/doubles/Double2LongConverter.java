package convention.transformers.doubles;

import net.sf.kfgodel.bean2bean.conversion.SpecializedTypeConverter;
import net.sf.kfgodel.bean2bean.exceptions.CannotConvertException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class Double2LongConverter implements SpecializedTypeConverter<Double, Long>{

	@Override
	public Long convertTo(Type expectedType, Double sourceObject,
			Annotation[] contextAnnotations) throws CannotConvertException {
		if (sourceObject == null) {
			// Por las dudas
			return null;
		}
		Long rounded = Math.round(sourceObject);
		return rounded;
	}

	public static Double2LongConverter create() {
		Double2LongConverter converter = new Double2LongConverter();
		return converter;
	}

}
