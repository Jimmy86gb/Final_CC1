package co.edu.udistrital.model.enums;

/**
 * Enum que representa los diferentes estados que puede tener
 * un reporte dentro del sistema durante su ciclo de atención.
 * Cada estado posee un nombre descriptivo para su visualización.
 * 
 * @author ChrZ
 */
public enum ReportStatus {

	PENDING("Pendiente"),
	ON_GOING("En progreso"),
	DONE("Atendida"),
	CANCELLED("Cancelada");

	private final String displayName;

	/**
	 * Constructor del enum.
	 *
	 * @param displayName Nombre descriptivo asociado al estado del reporte.
	 */
	private ReportStatus(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Obtiene el nombre descriptivo del estado del reporte.
	 *
	 * @return El nombre para visualización del estado.
	 */
	public String getDisplayName() {
		return displayName;
	}
}