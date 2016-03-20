/**
 * 
 */
package ar.com.kfgodel.transformbyconvention.api.tos;

/**
 * 
 * Representa un To que es convertible a un enum via Bean2Bean.
 * Para la coversion se utiliza el nombre del Enum, que en el TO 
 * es representado por el Id.
 * 
 * @author Lucas
 *
 */
public interface EnumTo {


	/**
	 * Devuelve el ID que identifica a la entidad (el nombre del enum)
	 * 
	 * @return El id, o null si el valor es 0
	 */
	String getId();

	/**
	 * Establece el ID de la entidad (el nombre del enum)
	 * 
	 * @param id
	 *            El Id que representa a la entidad
	 */
	void setId(String id);
}
