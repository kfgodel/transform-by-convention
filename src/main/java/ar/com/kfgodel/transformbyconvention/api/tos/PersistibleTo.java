/**
 * 07/06/2011 12:44:03 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.com.kfgodel.transformbyconvention.api.tos;


/**
 * Esta interfaz es una marca para todos aquellos TOs que representan una entidad persistible
 * identificable con ID.<br>
 * Este TO es convertible a una entidad concreta buscandola por ID. Si no existe se creará una nueva
 * con el constructor niládico.<br>
 * Indica la clase persistente relacionada con este To a través del annotation
 * {TransferObjectOf} para poder relacionar instancias de esta clase con su versión de dominio
 * 
 * @author D. García
 */
public interface PersistibleTo {

	/**
	 * Devuelve el ID que identifica a la entidad
	 * 
	 * @return El id, o null si el valor es 0
	 */
	public Long getId();

	/**
	 * Establece el ID de la entidad
	 * 
	 * @param id
	 *            El Id que representa a la entidad
	 */
	public void setId(Long id);
}
