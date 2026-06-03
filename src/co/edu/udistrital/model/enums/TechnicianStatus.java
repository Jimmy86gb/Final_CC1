package co.edu.udistrital.model.enums;

/**
 * Enum que representa los diferentes estados en los que puede encontrarse
 * un técnico dentro del sistema.
 * Cada estado posee un nombre descriptivo para su visualización.
 * 
 * @author ChrZ
 */
public enum TechnicianStatus {

	AVAILABLE("Disponible"),
	BUSY("Ocupado"),
	INACTIVE("Inactivo");

	private final String displayName;

	/**
	 * Constructor del enum.
	 *
	 * @param displayName Nombre descriptivo asociado al estado del técnico.
	 */
	private TechnicianStatus(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Obtiene el nombre descriptivo del estado del técnico.
	 *
	 * @return El nombre para visualización del estado.
	 */
	public String getDisplayName() {
		return displayName;
	}
}