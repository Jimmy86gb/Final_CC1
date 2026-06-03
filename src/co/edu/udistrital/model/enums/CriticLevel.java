package co.edu.udistrital.model.enums;

/**
 * Enum que representa los niveles de criticidad o prioridad
 * que puede tener un reporte dentro del sistema.
 * Cada nivel posee un nombre descriptivo para su visualización.
 * 
 * @author ChrZ
 */
public enum CriticLevel {

	LOW("Baja"),
	MEDIUM("Media"),
	HIGH("Alta");

	private final String displayName;

	/**
	 * Constructor del enum.
	 *
	 * @param displayName Nombre descriptivo asociado al nivel de criticidad.
	 */
	private CriticLevel(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Obtiene el nombre descriptivo del nivel de criticidad.
	 *
	 * @return El nombre para visualización del nivel de criticidad.
	 */
	public String getDisplayName() {
		return displayName;
	}
}