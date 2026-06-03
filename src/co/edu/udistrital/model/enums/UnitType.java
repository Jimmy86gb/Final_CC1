package co.edu.udistrital.model.enums;

/**
 * Enum que representa los diferentes tipos de unidades de servicio
 * disponibles dentro del sistema.
 * Cada tipo posee un nombre descriptivo para su visualización.
 * 
 * @author ChrZ
 */
public enum UnitType {

	CRANE("Grua"),
	MOTORCYCLE("Moto"),
	CAR("Carro"),
	TRUCK("Camioneta");

	private final String displayName;

	/**
	 * Constructor del enum.
	 *
	 * @param displayName Nombre descriptivo asociado al tipo de unidad.
	 */
	private UnitType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Obtiene el nombre descriptivo del tipo de unidad.
	 *
	 * @return El nombre para visualización del tipo de unidad.
	 */
	public String getDisplayName() {
		return displayName;
	}
}