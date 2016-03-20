/**
 * 
 */
package ar.com.kfgodel.transformbyconvention.api.tos;

import net.sf.kfgodel.bean2bean.annotations.CopyFrom;
import net.sf.kfgodel.bean2bean.interpreters.InterpreterType;

/**
 * Superclase base para la implementación de Tos basados en Enum
 * 
 * @author Lucas
 *
 */
public class EnumToSupport implements EnumTo {

	@CopyFrom(value = "name()", getterInterpreter = InterpreterType.GROOVY)
	private String id;
	
	/**
	 * @see EnumTo#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @see EnumTo#setId(String)
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

}
