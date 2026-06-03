package co.edu.udistrital.model.enums;

/**
 * Enum que representa los diferentes estados en los que puede encontrarse
 * una unidad o kit dentro del sistema.
 * Cada estado posee un nombre descriptivo para su visualización.
 * 
 * @author ChrZ
 */
public enum UnitStatus {

	AVAILABLE("Disponible"),
	ASSIGNED("Asignada"),
	MAINTENANCE("En mantenimiento"),
	INACTIVE("Inactivo");

	private final String displayName;

	/**
	 * Constructor del enum.
	 *
	 * @param displayName Nombre descriptivo asociado al estado de la unidad.
	 */
	private UnitStatus(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Obtiene el nombre descriptivo del estado de la unidad.
	 *
	 * @return El nombre para visualización del estado.
	 */
	public String getDisplayName() {
		return displayName;
	}
}