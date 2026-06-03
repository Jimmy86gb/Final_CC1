package co.edu.udistrital.model.dtos;

/**
 * DTO genérico para el transporte de pares ID-Valor hacia la vista.
 * Estrictamente estructurado sin lógica de interfaz gráfica.
 * 
 * @author Juan David Diaz Perez
 */
public class EntityItem {

	private final String id;
	private final String displayName;

	/**
	 * Constructor que crea una instancia de EntityItem.
	 *
	 * @param id Identificador único de la entidad.
	 * @param displayName Nombre descriptivo que será mostrado en la vista.
	 */
	public EntityItem(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	/**
	 * Obtiene el identificador de la entidad.
	 *
	 * @return El ID de la entidad.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Obtiene el nombre descriptivo de la entidad.
	 *
	 * @return El nombre para visualización.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Retorna la representación textual del objeto.
	 * Se utiliza para mostrar directamente el nombre descriptivo
	 * en componentes de interfaz.
	 *
	 * @return El nombre descriptivo de la entidad.
	 */
	@Override
	public String toString() {
		return displayName;
	}
}